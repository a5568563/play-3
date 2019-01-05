package com.play.service;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.play.entity.News;
import com.play.entity.Operation;
import com.play.entity.Section;
import com.play.entity.User;
import com.play.pojo.Article;
import com.play.repository.KeylistRepository;
import com.play.repository.NewsRepository;
import com.play.repository.OperationRepository;
import com.play.repository.SectionRepository;
import com.play.util.FileOperation;
import com.play.util.PageModel;
import com.play.util.Result;
import com.play.util.SortByValue;
import com.play.util.StringSpliter;

@Service
public class NewService {
	
	@Value("${web.text-upload-path}")
	String TextUploadPath;
	
	@Value("${web.static-path}")
	String BasePath;
	
	@Autowired
	NewsRepository repository;
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	KeylistRepository keylistRepository;
	
	@Autowired
	OperationRepository operationRepository;
	
	@Transactional
	String Delete_New (int new_id) {
		repository.deleteById(new_id);
		return "success";
	}
	
	
	/**
	 * 
	* @Title: GetTitleList   
	* @Description: 通过板块id和页数获取该页数上的新闻title
	* @param @param sid 板块id
	* @param @param PageNum	第几页
	* @param @return    设定文件   
	* @return Result    返回类型   
	* @throws   
	 */
	public Result GetTitleList(int sid, int PageNum){
		List<String> titleList = new ArrayList<String>();
		List<Integer> nidList = new ArrayList<Integer>();
		List<String> imgList = new ArrayList<String>();
		List<Integer> ctrList = new ArrayList<Integer>();
		Section section = sectionRepository.findById(sid).get();
		List<News> nList= repository.findBySection(section);
		Collections.sort(nList,new Comparator<News>() {
			@Override
			public int compare(News o1, News o2) {
				// TODO Auto-generated method stub
				return String.valueOf(o2.getHot()).compareTo(String.valueOf(o1.getHot()));
			}
		});
		Collections.sort(nList);

		for (News news : nList) {
			titleList.add(news.getTitle());
			nidList.add(news.getId());
			imgList.add(news.getTitle_picture());
			ctrList.add(news.getCTR());
		}
		PageModel titlePageModel = new PageModel(titleList);
		PageModel nidPageModel = new PageModel(nidList);
		PageModel ctrPageModel = new PageModel(ctrList);
		PageModel imgPageModel = new PageModel(imgList);
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title_page", titlePageModel);
		map.put("nid_page",nidPageModel);
		map.put("img_page",imgPageModel);
		map.put("ctr_page", ctrPageModel);
		result.setData(map);
		result.setStatus(true);
		return result;
	}
	
	/**
	 * 
	* @Title: Get_New   
	* @Description: 通过nid获取new
	* @param @param nid 
	* @param @return    设定文件   
	* @return Result    返回类型   
	* @throws   
	 * @throws IOException 
	 */
	public Result Get_New(int nid) throws IOException {
		Result result = new Result();
		News news = repository.findById(nid).get();
		Article article = new Article();
		article.setTitle(news.getTitle());
		article.setCreatetime(news.getCreatetime());
		article.setCtr(news.getCTR());
		article.setNid(news.getId());
		String section_name = sectionRepository.findById(news.getSection().getId()).get().getName();
		File file = new File(BasePath+TextUploadPath+"News/"+section_name+"/"+news.getContent());
		FileOperation fOperation = new FileOperation();
		String data = fOperation.readTxtFile(file);
		article.setContent(data);
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("data", article);
		result.setData(map);
		result.setStatus(true);
		return result;
	}
	
	/**
	 * 
	* @Title: CTR_Add   
	* @Description: CTR+1 , HOT+1
	* @param @param nid    设定文件   
	* @return void    返回类型   
	* @throws   
	 */
	public void CTR_Add (int nid){
		News news = repository.findById(nid).get();
		news.setCTR(news.getCTR()+1);
		news.setHot(news.getHot()+1);
		repository.save(news);
	}
	/**
	 * 
	* @Title: get_RecommendNews   
	* @Description: 获取用户推荐新闻列表   
	* @param @return    设定文件   
	* @return Result    返回类型   
	* @throws   
	 */
	public Result get_RecommendNews(User user){
		Result result = new Result();
		Sort sort = new Sort(Direction.DESC,"operate_time");
		Page<Operation> operationPage = operationRepository.findByUser(user, new PageRequest(0, 50));
		List<Section> AllSection = sectionRepository.findAll();
		Map<Integer, Map<String, Integer>> User_Section_Keylist = new HashMap<Integer, Map<String,Integer>>();
		for (Section section : AllSection) {
			Map<String, Integer>UserKeylist = Get_UserKeylist(operationPage, section.getId());
			User_Section_Keylist.put(section.getId(),UserKeylist);
		}
		
		Map<Integer, Double> news_proportion_temp = new HashMap<Integer, Double>(); //"section_id : 10条新闻占比 例如"1 : 0.85"
		double step = 1.0 / operationPage.getTotalElements();
		for (Operation operation : operationPage) {
			if (news_proportion_temp.get(operation.getNews().getSection().getId()) != null) {
				news_proportion_temp.put(operation.getNews().getSection().getId(), news_proportion_temp.get(operation.getNews().getSection().getId()) + step);
			} else {
				news_proportion_temp.put(operation.getNews().getSection().getId(), step);
			}
		}
		Map<Integer, Integer> news_proportion = get_News_proportion(news_proportion_temp);  //"section_id : 几条新闻 例如"1 : 8"
		PageModel recommendNews = get_recommenNews(news_proportion, User_Section_Keylist);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", recommendNews);
		result.setData(data);
		result.setStatus(true);
		return result;
	}
	
	public PageModel get_recommenNews(Map<Integer, Integer>news_proportion, Map<Integer, Map<String, Integer>> User_Section_Keylist ) {
		List<News> list = new ArrayList<News>();
		List<News> news_list = new ArrayList<News>();
		Iterator<Entry<Integer, Map<String, Integer>>> it = User_Section_Keylist.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, Map<String, Integer>> tempEntry = it.next();
			Section section = sectionRepository.findById(tempEntry.getKey()).get();
			List<Integer> nid_list = new ArrayList<Integer>();
			Set<News> sets = section.getNews();
			for (News news : sets) {
				nid_list.add(news.getId());
			}
			int length = nid_list.size();
			StringSpliter stringSpliter = new StringSpliter();
			Map<Integer, Double> map  = new HashMap<Integer, Double>();
			for (int i = 0; i < length; i++) {
				News n = repository.findById(nid_list.get(i)).get();
				String key_group = keylistRepository.findByNews(n).getKey_group();
				Map<String, Integer> new_keylist = stringSpliter.spliter(key_group);
				double cos = Cosine(tempEntry.getValue(), new_keylist, length);
				if (cos >= 1e-7) {
					map.put(n.getId(), cos);
				}
			}
			Iterator<Entry<Integer, Double>>iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Double>entry = iterator.next();
				News ns = repository.findById(entry.getKey()).get();
				list.add(ns);
			}
			Collections.sort(list,new Comparator<News>() {
				@Override
				public int compare(News o1, News o2) {
					// TODO Auto-generated method stub
					return String.valueOf(o2.getHot()).compareTo(String.valueOf(o1.getHot()));
				}
			});
			Collections.sort(list);
			
			for (News news : list) {
				int section_id = news.getSection().getId();
				int num = news_proportion.get(section_id);
				if(num >0){
					news_list.add(news);
					news_proportion.put(section_id, num-1);
				}else {
					continue;
				}
			}
		}
		PageModel pageModel = new PageModel(news_list);
		return pageModel;
	}

	public double Cosine(Map<String, Integer> User_KeyList, Map<String, Integer> News_KeyList, int total) {
		double cosine = 0;
		Iterator<Map.Entry<String, Integer>> it1 = User_KeyList.entrySet().iterator();
		Iterator<Map.Entry<String, Integer>> it2 = News_KeyList.entrySet().iterator();
		List<Integer>X = new ArrayList<Integer>();
		List<Integer>Y = new ArrayList<Integer>();
		while (it1.hasNext()) {
			Map.Entry<String, Integer> entry1 = it1.next();
			while (it2.hasNext()) {
				Map.Entry<String, Integer> entry2 = it2.next();
				if (entry1.getKey().equals(entry2.getKey())){
					System.out.println(entry1.getKey());
					X.add(entry1.getValue());
					Y.add(entry2.getValue());
				}
			}
		}
		int length = X.size();
		double sentence1 = 0;
		double sentence2 = 0;
		double sentence3 = 0;
		for (int i = 0; i < length; i++) {
			sentence1 += (X.get(i) * Y.get(i));
			sentence2 += (X.get(i) * X.get(i));
			sentence3 += (Y.get(i) * Y.get(i));
		}
		if (sentence2 ==0 || sentence3 ==0){
			return 0;
		}else{
			cosine = sentence1 / (Math.sqrt(sentence2)*Math.sqrt(sentence3));
			return cosine;
		}
	}
	
	public Map<Integer, Integer> get_News_proportion(Map<Integer, Double> news_proportion_temp) {
		List<Map.Entry<Integer, Double>> list = new ArrayList();
		Map<Integer, Integer>news_proportion = new HashMap<Integer, Integer>();
		Iterator<Map.Entry<Integer,Double>> it = news_proportion_temp.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Double> tempEntry = it.next();
			list.add(tempEntry);
		}
		Collections.sort(list,new SortByValue());
		for (int i = 0; i < 10; i++){
			int key = list.get(0).getKey();
			if (news_proportion.containsKey(key)) {
				news_proportion.put(key, news_proportion.get(key) + 1);
			} else {
				news_proportion.put(key, 1);
			}
			Entry<Integer, Double> tempEntry = list.remove(0);
			tempEntry.setValue(tempEntry.getValue() - 0.1);
			list.add(tempEntry);
			Collections.sort(list,new SortByValue());
		}
		return news_proportion;
	}
	
	
	public void out_map4(Map<Integer, Double> map){
		Iterator<Map.Entry<Integer,Double>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Double> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= "
                    + String.format("%.6f", entry.getValue()));
        }
	}
	/**
	 * 
	* @Title: Get_UserKeylist   
	* @Description: 通过用户近30次操作获取指定模块的用户关键词字典
	* @param @param operationPage
	* @param @param section_id
	* @param @return    设定文件   
	* @return Map<String,Integer>    返回类型   
	* @throws   
	 */
	public Map<String, Integer> Get_UserKeylist(Page<Operation> operationPage, int section_id){
		Map map = new HashMap<Integer, Integer>();  //map ("nid":"operation_weight")
		for (Operation operation : operationPage) {
			if (operation.getNews().getSection().getId() == section_id) {
				int nid = operation.getNews().getId();
				int value = operation.getOperate_type();
				if (map.get(nid)!=null){
					map.put(nid, (Integer)map.get(nid)+value);
				}else{
					map.put(nid, value);
				}
			} else {
				continue;
			}
		}
		HashMap<String, Integer> UserKeylist = new HashMap<String, Integer>();
		Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry<Integer, Integer> entry = it.next();
        	News news = repository.findById(entry.getKey()).get();
        	
        	String key_group = keylistRepository.findByNews(news).getKey_group();
        	StringSpliter strspliter = new StringSpliter();
        	Map<String, Integer> tempMap = strspliter.spliter(key_group);
        	addMap(UserKeylist, tempMap);
        }
        UserKeylist = hashMapSort(UserKeylist);
        //UserKeylist = cutMap(UserKeylist);
        return UserKeylist;
	}
	
	public void out_map1(Map<Integer, Integer>map){
		Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }
	}
	public void out_map2(Map<String, Integer>map){
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }
	}
	public void out_map3(Map<Integer, Map<String, Integer>> User_Section_Keylist){
		Iterator<Entry<Integer, Map<String, Integer>>> it = User_Section_Keylist.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Map<String, Integer>> entry = it.next();
			System.out.println("section = " + entry.getKey());
			out_map2(entry.getValue());
		}
	}
	public Map<String, Integer> addMap(Map<String, Integer>map1,Map<String, Integer>map2){
		Iterator<Map.Entry<String, Integer>> it = map2.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			if (map1.containsKey(entry.getKey())) {
				map1.put(entry.getKey(), map1.get(entry.getKey())+entry.getValue());
			} else {
				map1.put(entry.getKey(), entry.getValue());
			}
		}
		return map1;
	}
	/**
     * 
     * @param map HashMap<String, Integer> 按照值进行排序
     * @return：返回排序后的Map
     */
    public  HashMap<String, Integer>  hashMapSort(HashMap<String, Integer> map){
        //1、按顺序保存map中的元素，使用LinkedList类型
        List<Entry<String, Integer>> keyList = new LinkedList<Entry<String, Integer>>(map.entrySet()); 
        //2、按照自定义的规则排序
        Collections.sort(keyList, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                    if(o2.getValue().compareTo(o1.getValue())>0){  
                        return 1;  
                   }else if(o2.getValue().compareTo(o1.getValue())<0){  
                        return -1;  
                   }  else {
                       return 0;
                   }
            }        
        });
        //3、将LinkedList按照排序好的结果，存入到HashMap中
        HashMap<String,Integer> result=new LinkedHashMap<>();
        for(Entry<String, Integer> entry:keyList){
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
    
    public HashMap<String, Integer> cutMap (HashMap<String, Integer> map) {
    	List<Entry<String, Integer>> keyList = new LinkedList<Entry<String, Integer>>(map.entrySet()); 
    	HashMap<String,Integer> result=new LinkedHashMap<>();
    	int i = 0;
    	for(Entry<String, Integer> entry:keyList){
            result.put(entry.getKey(),entry.getValue());
            i++;
            if(i >= 10){
            	break;
            }
        }
    	return result;
    }
    
    public Result Cold_Start(String interest) {
		Result result = new Result();
		StringSpliter spliter = new StringSpliter();
		List<Integer> list = spliter.spliter_interest(interest);
		int length = list.size();
		List<News> temp = repository.findAll();
		Collections.sort(temp,new Comparator<News>() {
			@Override
			public int compare(News o1, News o2) {
				// TODO Auto-generated method stub
				return String.valueOf(o2.getHot()).compareTo(String.valueOf(o1.getHot()));
			}
		});
		Collections.sort(temp);
		List<News> news_list = new ArrayList<News>();
		if (length == 1) {
			int count = 0;
			int size = temp.size();
			for (int i = 0; i<size;i++){
				if (count >= 10) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(0)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
		} else if (length == 2){
			int count = 0;
			int size = temp.size();
			for (int i = 0; i<size;i++){
				if (count >= 5) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(0)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
			count = 0;
			for (int i = 0; i<size;i++){
				if (count >= 5) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(1)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
		} else if (length == 3) {
			int count = 0;
			int size = temp.size();
			for (int i = 0; i<size;i++){
				if (count >= 4) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(0)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
			count = 0;
			for (int i = 0; i<size;i++){
				if (count >= 3) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(1)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
			count = 0;
			for (int i = 0; i<size;i++){
				if (count >= 3) {
					break;
				}
				if (temp.get(i).getSection().getId() == list.get(2)) {
					news_list.add(temp.get(i));
					count++;
				} else {
					continue;
				}
			}
		}
		Map<String, Object>data = new HashMap<String, Object>();
		PageModel pageModel = new PageModel(news_list);
		data.put("data",pageModel);
		result.setData(data);
		result.setStatus(true);
		return result;
	}
    
}
