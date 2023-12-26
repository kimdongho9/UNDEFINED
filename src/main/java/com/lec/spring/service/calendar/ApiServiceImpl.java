package com.lec.spring.service.calendar;

import com.lec.spring.domain.calendar.Api;
import com.lec.spring.repository.calendar.ApiRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService {

    private ApiRepository apiRepository;

    @Value("${API.servicekey}")
    private String servicekey;

    private String url1 = "http://openapi.q-net.or.kr/api/service/rest/InquiryTestInformationNTQSVC/getJMList?jmCd=";
    private String url2 = "http://openapi.q-net.or.kr/api/service/rest/InquiryTestInformationNTQSVC/getFeeList?jmCd=";

    public ApiServiceImpl(SqlSession sqlSession){
        apiRepository = sqlSession.getMapper(ApiRepository.class);
        System.out.println(getClass().getName() + "()생성!");
    }

    @Override
    public List<Api> list() {
        return apiRepository.findAll();
    }

    @Override
    public int save(Api api) {
        apiRepository.save(api);
        return 1;
    }





    private static String getTagValue(String tag, Element eElement) {
        NodeList nList = null;
        Node nValue = null;
        try {
            nList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            nValue = (Node) nList.item(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }



    /*@Scheduled(fixedDelay = 3600000)
    public void get1320() {
        String url1_1 = url1 + 1320 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 1320 +"&serviceKey="+ servicekey;



        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

 /*   @Scheduled(fixedDelay = 3600000)
    public void get2290() {
        String url1_1 = url1 + 2290 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 2290 +"&serviceKey="+ servicekey;



        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

  /*  @Scheduled(fixedDelay = 3600000)
    public void get6921() {
        String url1_1 = url1 + 6921 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 6921 +"&serviceKey="+ servicekey;



        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }*/

/*   @Scheduled(fixedDelay = 3600000)
    public void get0622() {
        String url1_1 = url1 + 0622 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 0622 +"&serviceKey="+ servicekey;



        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* @Scheduled(fixedDelay = 3600000)
    public void get0601() {
        String url1_1 = url1 + 0601 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 0601 +"&serviceKey="+ servicekey;


        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
   /* @Scheduled(fixedDelay = 3600000)
    public void get2170() {
        String url1_1 = url1 + 2170 +"&serviceKey="+ servicekey;
        String url2_1 = url2 + 2170 +"&serviceKey="+ servicekey;



        String fee = null;
        List<Api> apis = new ArrayList<>() ;
        List<Map<String, String>> list = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documentInfo = builder.parse(url2_1);

            documentInfo.getDocumentElement().normalize();

            String rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            Element root = documentInfo.getDocumentElement();
            NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

            for (int i = 0; i < nList.getLength(); i++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;

                fee = getTagValue("contents", eElement);
            }

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            documentInfo = builder.parse(url1_1);

            documentInfo.getDocumentElement().normalize();

            rootElementName = documentInfo.getDocumentElement().getNodeName();
            System.out.println("Root: " + rootElementName);
            root = documentInfo.getDocumentElement();
            nList = root.getElementsByTagName("items").item(0).getChildNodes();
            Api api = null;
            for (int j = 0; j < nList.getLength(); j++) {
                Map<String, String> map = new HashMap<>();
                Node nNode = nList.item(j);
                Element eElement = (Element) nNode;
                api = api.builder()
                        .implplannm(getTagValue("implplannm", eElement))
                        .jmfldnm(getTagValue("jmfldnm", eElement))
                        .docexamenddt(getTagValue("docexamenddt", eElement))
                        .docexamstartdt(getTagValue("docexamstartdt", eElement))
                        .docpassdt(getTagValue("docpassdt", eElement))
                        .docregenddt(getTagValue("docregenddt", eElement))
                        .docregstartdt(getTagValue("docregstartdt", eElement))
                        .pracexamenddt(getTagValue("pracexamenddt", eElement))
                        .pracexamstartdt(getTagValue("pracexamstartdt", eElement))
                        .pracpassenddt(getTagValue("pracpassenddt", eElement))
                        .pracpassstartdt(getTagValue("pracpassstartdt", eElement))
                        .pracregenddt(getTagValue("pracregenddt", eElement))
                        .pracregstartdt(getTagValue("pracregstartdt", eElement))
                        .fee(fee)
                        .build();

                map.put("docexamenddt", getTagValue("docexamenddt", eElement));
                map.put("docexamstartdt", getTagValue("docexamstartdt", eElement));
                map.put("docpassdt", getTagValue("docpassdt", eElement));
                map.put("docregenddt", getTagValue("docregenddt", eElement));
                map.put("docregstartdt", getTagValue("docregstartdt", eElement));
                map.put("implplannm", getTagValue("implplannm", eElement));
                map.put("jmfldnm", getTagValue("jmfldnm", eElement));
                map.put("pracexamenddt", getTagValue("pracexamenddt", eElement));
                map.put("pracexamstartdt", getTagValue("pracexamstartdt", eElement));
                map.put("pracpassenddt", getTagValue("pracpassenddt", eElement));
                map.put("pracpassstartdt", getTagValue("pracpassstartdt", eElement));
                map.put("pracregenddt", getTagValue("pracregenddt", eElement));
                map.put("pracregstartdt", getTagValue("pracregstartdt", eElement));
                map.put("fee", fee);

                list.add(map);
                apis.add(api);

                apiRepository.save(api);
            }

            System.out.println(apis.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


}
