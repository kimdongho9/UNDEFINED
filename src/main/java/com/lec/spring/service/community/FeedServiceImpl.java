package com.lec.spring.service.community;

import com.lec.spring.domain.user.User;
import com.lec.spring.domain.community.Feed;
import com.lec.spring.domain.community.Photo;
import com.lec.spring.domain.community.FeedTag;
import com.lec.spring.repository.community.*;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FeedServiceImpl implements FeedService {

    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;

    @Value("${app.upload.path}")
    private String uploadDir;

    FeedRepository feedRepository;
    CommentRepository commentRepository;
    TagRepository tagRepository;
    LikeRepository likeRepository;
    PhotoRepository photoRepository;

    public FeedServiceImpl(SqlSession sqlSession) {
        this.feedRepository = sqlSession.getMapper(FeedRepository.class);
        this.commentRepository = sqlSession.getMapper(CommentRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);
        this.likeRepository = sqlSession.getMapper(LikeRepository.class);
        this.photoRepository = sqlSession.getMapper(PhotoRepository.class);

        System.out.println("FeedServiceImpl() 생성");
    }

    // 해당 피드의 태그 목록을 저장
    public void setTagListPerFeed(List<Feed> list) {
        for(var feed : list) {
            // 해당 피드의 태그 목록을 스트링으로 저장
            // 이거 tag repository로 바꾸자
            List<String> tagList = feedRepository.findTagsByFeedId(feed.getFeedId());
            StringBuffer sb = new StringBuffer();
            tagList.forEach(tag -> sb.append("#").append(tag).append(" "));
            feed.setTagList(sb.toString());
//            System.out.println("tag : " + feed.getTagList());
        }
    }

    // 피드의 내용이 넘칠 때, 축소본
    public void setShortContentPerFeed(List<Feed> list) {
        for(var feed : list) {
            // 피드의 내용이 100자를 넘기게 된다면 축소분을 초기화시키자.
            if(feed.getFeedContent().length() > 100) {
                feed.setShortContent(feed.getFeedContent().substring(0, 100));
            }

        }
    }

    // 피드의 likeUserList 초기화
    public void setLikeUserList(List<Feed> list) {
        for(var feed : list) {
            feed.setLikeUserList(likeRepository.findUsers(feed.getFeedId()));
        }
    }

    public void setFileList(List<Feed> list) {
        for(var feed : list) {
            List<Photo> fileList = photoRepository.findByFeed(feed.getFeedId());
            setImage(fileList);
            feed.setFileList(fileList);
        }
    }

    private void setImage(List<Photo> fileList) {
        String realPath = new File(uploadDir).getAbsolutePath();

        for(var file : fileList) {
            BufferedImage imgData = null;
            File f = new File(realPath, file.getFilename());

            try {
                imgData = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println("file is not exist");
                throw new RuntimeException(e);
            }

            if(imgData != null) file.setImage(true);
        }
    }

    @Override
    public List<Feed> list(Integer page, Model model) {

        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        // 페이징
        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        session.setAttribute("page", page);

        long cnt = feedRepository.countAll();
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);

        int startPage = 0;
        int endPage = 0;

        List<Feed> list = null;

        if(cnt > 0){
            if(page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당페이지의 글 목록 읽어오기
            LocalDateTime start = LocalDateTime.now();
            list = feedRepository.findAllCompFeedFromRow(fromRow, pageRows);
            if(list != null) {
                setShortContentPerFeed(list);
                setTagListPerFeed(list);
                setLikeUserList(list);
                setFileList(list);
            }
            LocalDateTime end = LocalDateTime.now();
            model.addAttribute("list", list);

            Duration diff = Duration.between(start.toLocalTime(), end.toLocalTime());
            model.addAttribute("searchTime", diff.toMillis() / 1000.00);
        } else {
            page = 0;
        }

        model.addAttribute("option", "all");
        model.addAttribute("totalCnt", cnt);  // 전체 글 개수
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

    @Override
    public void listByOption(String option, String keyword, Integer page, Model model) {
        HttpSession session = U.getSession();
        if(option == null) option = (String) session.getAttribute("searchOption");
        else session.setAttribute("searchOption", option);

        if(keyword == null) keyword = (String) session.getAttribute("keyword");
        else session.setAttribute("keyword", keyword);

        if(!keyword.isEmpty()) {
            switch (option) {
                case "nick" -> listByNickname(keyword, page, model);
                case "tag" -> listByTag(keyword, page, model);
                default -> listByAll(keyword, page, model);
            }
        } else {
            session.setAttribute("searchOption", "all");
            list(page, model);
        }
    }


    // 해당 Nickname을 가진 유저가 작성한 모든 피드글 가져오기
    // 추후에 완료글 리스트에서도 사용
    @Override
    public List<Feed> listByNickname(String nickname, Integer page, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);

        long cnt = feedRepository.countAllByNickname(nickname);   // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        int startPage = 0;
        int endPage = 0;

        List<Feed> list = null;

        if(cnt > 0){
            if(page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            LocalDateTime start = LocalDateTime.now();
            list = feedRepository.listByNicknameFromRow(nickname, fromRow, pageRows);
            if(list != null) {
                setShortContentPerFeed(list);
                setTagListPerFeed(list);
                setLikeUserList(list);
                setFileList(list);
            }
            LocalDateTime end = LocalDateTime.now();
            model.addAttribute("list", list);

            Duration diff = Duration.between(start.toLocalTime(), end.toLocalTime());
            model.addAttribute("searchTime", diff.toMillis() / 1000.00);
        } else {
            page = 0;
        }

        model.addAttribute("option", "nick");
        model.addAttribute("totalCnt", cnt);  // 전체 글 개수
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

    @Override
    public List<Feed> listByTag(String tag, Integer page, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);

        Set<Long> feedIdList = tagRepository.feedIdListByTag(tag);
        long cnt = feedRepository.countAllByTag(feedIdList);   // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        int startPage = 0;
        int endPage = 0;

        List<Feed> list = null;

        if(cnt > 0){
            if(page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            LocalDateTime start = LocalDateTime.now();
            list = feedRepository.listByTagFromRow(feedIdList, fromRow, pageRows);
            if(list != null) {
                setShortContentPerFeed(list);
                setTagListPerFeed(list);
                setLikeUserList(list);
                setFileList(list);
            }
            LocalDateTime end = LocalDateTime.now();
            model.addAttribute("list", list);

            Duration diff = Duration.between(start.toLocalTime(), end.toLocalTime());
            model.addAttribute("searchTime", diff.toMillis() / 1000.00);
        } else {
            page = 0;
        }

        model.addAttribute("option", "tag");
        model.addAttribute("totalCnt", cnt);  // 전체 글 개수
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

    @Override
    public List<Feed> listByAll(String keyword, Integer page, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);

        Set<Long> feedIdList = tagRepository.feedIdListByTag(keyword);
        // 해당 키워드를 닉네임으로 가지는 feed_id 추가
        if(feedIdList != null) {
            feedIdList.addAll(feedRepository.feedIdListByNickname(keyword));
        } else {
            feedIdList = feedRepository.feedIdListByNickname(keyword);
        }


        long cnt = feedRepository.countAllByTag(feedIdList);   // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        int startPage = 0;
        int endPage = 0;

        List<Feed> list = null;

        if(cnt > 0){
            if(page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            LocalDateTime start = LocalDateTime.now();
            list = feedRepository.listByAllFromRow(feedIdList, fromRow, pageRows);
            if(list != null) {
                setShortContentPerFeed(list);
                setTagListPerFeed(list);
                setLikeUserList(list);
                setFileList(list);
            }
            LocalDateTime end = LocalDateTime.now();
            model.addAttribute("list", list);

            Duration diff = Duration.between(start.toLocalTime(), end.toLocalTime());
            model.addAttribute("searchTime", diff.toMillis() / 1000.00);
        } else {
            page = 0;
        }

        model.addAttribute("option", "all");
        model.addAttribute("totalCnt", cnt);  // 전체 글 개수
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

    // 특정 id의 피드 글 불러오는 메소드
    // update 시 기존의 내용을 보여주기 위함
    @Override
    public Feed findFeedById(Long id) {
        List<Feed> feed = List.of(feedRepository.findFeedById(id));
        setTagListPerFeed(feed);
        setShortContentPerFeed(feed);
        setFileList(feed);

        return feed.get(0);
    }

    @Override
    public List<Feed> listByUserId(Long userId, Integer page, Model model, String state) {

        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);

        long cnt = feedRepository.countFeedByUserId(userId, state);   // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        int startPage = 0;
        int endPage = 0;

        List<Feed> list = null;

        if(cnt > 0){
            if(page > totalPage) page = totalPage;
            int fromRow = (page - 1) * pageRows;

            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            LocalDateTime start = LocalDateTime.now();
            list = feedRepository.myFeedFromRow(userId, fromRow, pageRows, state);
            if(list != null) {
                setShortContentPerFeed(list);
                setTagListPerFeed(list);
                setLikeUserList(list);
                setFileList(list);
            }
            LocalDateTime end = LocalDateTime.now();
            model.addAttribute("list", list);

            Duration diff = Duration.between(start.toLocalTime(), end.toLocalTime());
            model.addAttribute("searchTime", diff.toMillis() / 1000.00);
        } else {
            page = 0;
        }

        model.addAttribute("totalCnt", cnt);  // 전체 글 개수
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


    // feed 추가/삭제/수정

    // feed 추가 -> comp, temp 구분 / del => comp 를 del 로 바꾸는 repository 메소드로
    // feed 추가하기 위해 뭐가 필요?
    // => feed 테이블에 들어갈 내용 
    // User : 이건 흠...일단 나중에 security 구현하면서 해보자 : 임의로 feed의 user 세팅 필요
    // 첨부 파일에 관해 다루는 메소드도 필요
    @Override
    public int writeFeed(Feed feed, Long userId, List<MultipartFile> files) {

        // add 하기 위해서 필요한 작업
        // user 초기화
        // tag_id 생성 작업, 해당 태그와 작성된 feed_id를 연결하는 작업
        // 파일 관련 작업
        feed.setUser(User.builder()
                        .id(userId)
                        .build());

        // 임시 저장 글이고 내용이 존재하지 않는 경우에는 빈 문자열을 추가
        if(feed.getFeedState().equals("temp")) {
            if(feed.getFeedContent().isEmpty()) {
                feed.setFeedContent("");
            }
        }

        // feed를 추가
        int result = feedRepository.saveFeed(feed);

        // Tag 추가 코드
        if(result == 1) {
            result = addTagForFeed(feed);
        }

        // 첨부 파일 추가
        addFiles(files, feed.getFeedId());

        return result;
    }

    private void addFiles(List<MultipartFile> files, Long feedId) {
        if(files != null) {
            for(var e : files){
                // 물리적인 파일 저장
                Photo photo = upload(e);

                // 성공하면 DB 에도 저장
                if(photo != null){
                    photo.setFeedId(feedId);   // FK 설정
                    System.out.println("photo : " + photo);
                    photoRepository.save(photo);   // INSERT
                }
            }
        }
    }

    private Photo upload(MultipartFile multipartFile) {
        Photo photo = null;

        // 담긴 파일이 없으면 pass
        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename == null || originalFilename.length() == 0) return null;

        // 원본파일명
        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // 저장될 파일명
        String fileName = sourceName;

        // 파일명 이 중복되는지 확인
        File file = new File(uploadDir, sourceName);
        if(file.exists()){  // 이미 존재하는 파일명,  중복되면 다름 이름으로 변경하여 저장
            // a.txt => a_2378142783946.txt  : time stamp 값을 활용할거다!
            int pos = fileName.lastIndexOf(".");
            if(pos > -1){   // 확장자가 있는 경우
                String name = fileName.substring(0, pos);  // 파일 '이름'
                String ext = fileName.substring(pos + 1);   // 파일 '확장자'

                // 중복방지를 위한 새로운 이름 (현재시간 ms) 를 파일명에 추가
                fileName = name + "_" + System.currentTimeMillis() + "." + ext;
            } else {  // 확장자가 없는 경우
                fileName += "_" + System.currentTimeMillis();
            }
        }

        // java.nio
        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());
        System.out.println(copyOfLocation);

        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다

            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING    // 기존에 존재하면 덮어쓰기
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        photo = Photo.builder()
                .filename(fileName)   // 저장된 이름
                .sourcename(sourceName)  // 원본 이름
                .build();

        return photo;
    }

    private int addTagForFeed(Feed feed) {

        Long feedId = feed.getFeedId();
        int result = 1;
        if(!feed.getTagList().isEmpty()) {
            // tag list 만들기
            List<String> taglist = List.of(feed.getTagList().trim().split(","));

            for(var tag : taglist) {
                FeedTag isExist = tagRepository.findTagByName(tag.trim());
                Long tagId = 0L;
                if(isExist == null) {
                    // 새로운 tag 일 시, 저장하고 pk 값 받아오기
                    FeedTag newFeedTag = new FeedTag();
                    newFeedTag.setTagName(tag.trim());
                    System.out.println("here??");
                    int success = tagRepository.addTag(newFeedTag);

                    if(success == 1) tagId = newFeedTag.getTagId();

                } else {
                    tagId = isExist.getTagId();
                }

                result = tagRepository.addTagAndFeed(feedId, tagId);
                if(result == 0) return result;
            }
        } else {
            feed.setTagList("");
        }

        return result;
    }
    
    
    // 피드 삭제
    // 아마 delete CASCADE라 해당 피드를 참조하는 다른 테이블의 데이터도 삭제
    // 요놈은 진짜 삭제하는 놈임
    // 임시저장, 휴지통에서 삭제할 때 쓰느 메소드
    
    // 하나만 삭제
    @Override
    public int deleteFeed(Long feedId){
        int result = 0;
        Feed feed = feedRepository.findFeedById(feedId);

        if(feed != null) {
            List<Photo> fileList = photoRepository.findByFeed(feedId);
            if(fileList != null) {
                for(Photo file : fileList) {
                    delFile(file);
                }
            }

            result = feedRepository.deleteFeedById(feedId);
        }

        return result;
    }

    // 모두 삭제
    @Override
    public int deleteFeedAllByUserId(Long userId, String state) {
        List<Feed> feedList = feedRepository.findDelFeedByUserId(userId, state);
        int result = 1;

        if(feedList != null) {
            for(Feed feed : feedList) {
                result = deleteFeed(feed.getFeedId());
                if(result == 0) return result;
            }
        }

        return result;
    }

    // 휴지통 보내기
    // 해당 피드의 state 만 comp -> del 로
    // 임시 저장글은 삭제 시 db에서 완전 삭제됨 (휴지통X)
    
    // 하나만 삭제
    @Override
    public int trashFeed(Long feedId) {
        int result = 0;
        Feed feed = feedRepository.findFeedById(feedId);

        if(feed != null) {
            feed.setFeedState("del");
            result = feedRepository.updateFeed(feed);
        }
        return result;
    }

    // 모두 삭제
    @Override
    public int trashFeedAllByUserId(Long userId) {

        List<Feed> feedList = feedRepository.findAllCompFeedByUserId(userId);

        for(var feed : feedList) {
            feed.setFeedState("del");
            if(feedRepository.updateFeed(feed) == 0) return 0;
        }

        return 1;
    }

    // 피드 복구
    // 해당 피드의 state만 del -> comp로
    @Override
    public int restoreFeed(Long feedId) {
        int result = 0;
        Feed feed = feedRepository.findFeedById(feedId);

        if(feed != null) {
            feed.setFeedState("comp");
            result = feedRepository.updateFeed(feed);
        }

        return result;
    }

    // 피드 수정
    @Override
    public int updateFeed(Feed feed, List<MultipartFile> newFiles, Long[] deleteList) {
        System.out.println("update feed : " + feed.getFeedId());
        System.out.println("update feed tag: " + feed.getTagList());
        System.out.println("update feedDTO : " + feed);
        int result = feedRepository.updateFeed(feed);

        if(feed.getFeedState().equals("temp")) {
            if(feed.getFeedContent().isEmpty()) {
                feed.setFeedContent("");
            }
        }

        // 태그 수정
        if(result == 1) {
            result = editTagForFeed(feed);
        }
        
        // 새로운 첨부 파일 추가
        addFiles(newFiles, feed.getFeedId());
        
        // 삭제할 첨부파일 삭제
        if(deleteList != null) {
            for(Long fileId : deleteList) {
                Photo file = photoRepository.findById(fileId);
                if(file != null) {
                    delFile(file);
                    photoRepository.delete(file);
                }
            }
        }
        return result;
    }

    private void delFile(Photo file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();
        File f = new File(saveDirectory, file.getFilename());  // 물리적으로 저장된 파일들이 삭제 대상
        System.out.println("삭제시도--> " + f.getAbsolutePath());

        if(f.exists()){
            if(f.delete()){
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    private int editTagForFeed(Feed feed) {

        Long feedId = feed.getFeedId();
        int result = 1;

        if(!feed.getTagList().isEmpty()) {
            // tag list 만들기
            List<String> taglist = List.of(feed.getTagList().trim().split(","));

            // 해당 태그를 기존의 feed 가 가지고 있는지 검사 => origin_tag_id_list , new_tag_id_list
            List<Long> originTagList = tagRepository.findTagIdByFeedIDd(feedId);
            List<Long> newTagList = new ArrayList<>();
            for (var tag : taglist) {
                // tag 의 id 값 가져오기
                FeedTag isExist = tagRepository.findTagByName(tag.trim());
                Long tagId = 0L;
                if (isExist == null) {
                    // 새로운 tag 일 시, 저장하고 pk 값 받아오기
                    FeedTag newFeedTag = new FeedTag();
                    newFeedTag.setTagName(tag.trim());
                    int success = tagRepository.addTag(newFeedTag);

                    if (success == 1) tagId = newFeedTag.getTagId();

                } else {
                    tagId = isExist.getTagId();
                }

                newTagList.add(tagId);
            }

            // 비교 들가자
            // origin 태그의 new 태그가 없으면? 추가
            for (var id : newTagList) {
                if (!originTagList.contains(id)) {
                    result = tagRepository.addTagAndFeed(feedId, id);
                    if (result == 0) return 0;
                }
            }

            // new 태그의 origin 태그가 없으면? 삭제
            for (var id : originTagList) {
                if (!newTagList.contains(id)) {
                    result = tagRepository.deleteTagAndFeed(feedId, id);
                    if (result == 0) return 0;
                }
            }
        } else {
            feed.setTagList("");
        }

        return result;
    }

}
