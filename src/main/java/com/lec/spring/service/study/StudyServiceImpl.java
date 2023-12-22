package com.lec.spring.service.study;

import com.lec.spring.domain.study.Favor;
import com.lec.spring.domain.study.Post;
import com.lec.spring.domain.study.Skill;
import com.lec.spring.domain.user.User;
import com.lec.spring.repository.study.*;
import com.lec.spring.repository.user.UserRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class StudyServiceImpl implements StudyService {

    @Value("${app.pagination.study_rows}")
    private int PAGE_ROWS;

    @Value("${app.pagination.study_pages}")
    private int WRITE_PAGES;

    private PostRepository postRepository;
    private TitleSearchRepository titleSearchRepository;
    private PostSearchRepository postSearchRepository;
    private SkillRepository skillRepository;
    private FavorRepository favorRepository;
    private UserRepository userRepository;

    public StudyServiceImpl(SqlSession sqlSession){
        postRepository = sqlSession.getMapper(PostRepository.class);
        skillRepository = sqlSession.getMapper(SkillRepository.class);
        favorRepository = sqlSession.getMapper(FavorRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);

        titleSearchRepository = sqlSession.getMapper(TitleSearchRepository.class);
        postSearchRepository = sqlSession.getMapper(PostSearchRepository.class);
        System.out.println("스터디 서비스 생성");
    }

    @Override
    public int write(Post post, String skillNames) {
        // Datetime 바꾸는 로직이 필요할꺼 같다.
        // LocalDateTime => string -> date 바꾸는 메소드가 있을꺼 같습니다
        // setEndDate()
        post.setDataForEnddate(post.getDataForEnddate() + " 00:00:00");
        post.setEnddate(LocalDateTime.parse(post.getDataForEnddate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        post.setDataForStartdate(post.getDataForStartdate() +" 00:00:00");
        post.setStartdate(LocalDateTime.parse(post.getDataForStartdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //System.out.println(skillNames);



        // 해당 study_skills 에 데이터 넣기
        // 데이터 : post_id, skill_id
        // skill_id 찾았음
        // 넣었음
        User user = U.getLoggedUser();

        user = userRepository.findById(user.getId());
        post.setUser(user);



        int result =  postRepository.save(post);
        // post_id : post.getId();
        // skill_id : skill.getSkillId();


        Arrays.stream(skillNames.trim().split(" ")).toList().forEach((e) -> {
            //System.out.println(e);
            Skill skill = skillRepository.findByName(e);
            skillRepository.saveSkills(skill.getSkillId(), post.getId());
        });


        return result;
    }

    @Override
    @Transactional
    public Post detail(Long id) {
        postRepository.incViewCnt(id);
        Post post = postRepository.findById(id);
        setSkillList(List.of(post));
        return post;
    }

    @Override
    public List<Post> list(Integer page, Model model) {
        return listByOption(postRepository, page, model, "", null);
    }
    @Override
    public List<Post> listByKeyword(Integer page, Model model, String keyword) {
        model.addAttribute("keyword", keyword);
        return listByOption(titleSearchRepository, page, model, keyword, null);
    }

    @Override
    public List<Post> listBySkill(Integer page, Model model, String skill) {

        model.addAttribute("skillOption", skill);

        List<Long> postList = skillRepository.findSkillIdsByName(List.of(skill.trim().split(",")));
        if(postList.isEmpty()) postList.add(0L);
        return listByOption(postSearchRepository, page, model, "", postList);
    }

    @Override
    public List<Post> listByFavor(
            Integer page
            , Model model
            , String skill
            , String favor
            , Long userId
    ) {

        model.addAttribute("skillOption", skill);
        model.addAttribute("favor", favor);
        List<Long> postList =
                new java.util.ArrayList<>(skillRepository.findSkillIdsByName(List.of(skill.trim().split(",")))
                        .stream()
                        .filter(postId -> favorRepository.isFavor(postId, userId) != null)
                        .toList());
        if(postList.isEmpty()) postList.add(0L);
        return listByOption(postSearchRepository, page, model, "", postList);
    }

    @Override
    public List<Post> favorByAll(Integer page, Model model, String skill, String favor, Long userId) {

        model.addAttribute("skillOption", skill);
        model.addAttribute("favor", favor);

        List<Long> list = new java.util.ArrayList<>(postRepository.findAll()
                .stream()
                .map(Post::getId)
                .filter(id -> favorRepository.isFavor(id, userId) != null)
                .toList());

        System.out.println("favor list : " + list);
        if(list.isEmpty()) list.add(0L);
        return listByOption(postSearchRepository, page, model, "", list);
    }

    private List<Post> listByOption(
            SearchRepository searchRepository,
            Integer page,
            Model model,
            String keyword,
            List<Long> postList
    ) {
// 현재 페이지 parameter
        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);
        long cnt = searchRepository.countAll(keyword, postList);   // 글 목록 전체의 개수
        //System.out.println("cnt : " + cnt);
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 페이지의 글 목록
        List<Post> list = null;

        if(cnt > 0){  // 데이터가 최소 1개 이상 있는 경우만 페이징
            //  page 값 보정
            if(page > totalPage) page = totalPage;

            // 몇번째 데이터부터 fromRow
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;
            // 해당페이지의 글 목록 읽어오기
            list = searchRepository.selectFromRow(fromRow, pageRows, keyword, postList);
            setSkillList(list);

            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        return list;
    }



    public void setSkillList(List<Post> list) {
        list.forEach(post -> {
            post.setSkillList(skillRepository.findSkillsByPostId(post.getId()));
        });
    }

    @Override
    public Post selectById(long id) {
        return postRepository.findById(id);
    }

    @Override
    public int update(Post post, String skillNames) {
        post.setDataForEnddate(post.getDataForEnddate() + " 00:00:00");
        post.setEnddate(LocalDateTime.parse(post.getDataForEnddate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        post.setDataForStartdate(post.getDataForStartdate() +" 00:00:00");
        post.setStartdate(LocalDateTime.parse(post.getDataForStartdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<Skill> list = skillRepository.findSkillsByPostId(post.getId());    // 이미 있던 데이터
        String skillNames2 = skillNames.trim();
        for (var x : skillNames2.split(" ")) {
            boolean skillExists = false;
            for (var skill : list) {
                if (skill.getSkillName().equals(x)) {
                    skillExists = true;
                    break;
                }
            }
            if (!skillExists) {
                // 스킬이 없으면 추가
                Skill skill = skillRepository.findByName(x);
                skillRepository.saveSkills(skill.getSkillId(), post.getId());
            }
        }
        for (var skill : list) {
            boolean skillExists = false;
            for (var x :skillNames2.split(" ")) {
                if (skill.getSkillName().equals(x)) {
                    skillExists = true;
                    break;
                }
            }
            if (!skillExists) {
                skillRepository.delSkills(skill.getSkillId(), post.getId());
            }
        }

        return postRepository.update(post);
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;
        Post post = postRepository.findById(id); // 존재하는 데이터인지 읽어온다
        if(post != null){ // 존재한다면 삭제 진행
            result = postRepository.delete(post);
        }
        return result;
    }


    //skills
    @Override
    public List<Skill> getSkillsByPostId(Long id) {
        return postRepository.getSkillsByPostId(id);
    }

    //즐겨찾기
    @Override
    public List<Favor> getFavorByUserId(Long id){ return favorRepository.findByUserId(id);}

    public int Favsave(Long postid, Long userid){return favorRepository.save(postid, userid);}
    public int Favdelete(Long postid, Long userid){return favorRepository.delete(postid, userid);}

    @Override
    public List<Post> listForMyPage(Long id) {
        List<Post> list = postRepository.findAll()
                .stream()
                .filter(post -> favorRepository.isFavor(post.getId(), id) != null)
                .toList();
        setSkillList(list);
        return list;
    }
}
