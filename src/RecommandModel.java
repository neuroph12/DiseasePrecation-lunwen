import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RecommandModel {
	//动漫映射卡通
	/*
	static String tags="计算机,设计,金融,法律,医学,物理,化学,历史,地理,哲学,吉他,钢琴,小提琴,古筝,二胡,琵琶,乐理,编曲,舞蹈,画画,折纸,国学,书法,摄影,汉语,英语,法语,日语,"+
"韩语,德语,俄语,西班牙语,意大利语,葡萄牙语,跑步,徒步,自行车,篮球,足球,羽毛球,乒乓球,桌面足球,桌球,棒球,游泳,爬山"+
			",电影,唱歌,动漫展览,棋牌,桌面游戏,购物,吃饭,点心,烧烤,烹饪";
			*/
	static String tags="男人,女人,婴儿,青年,老年,中年";
	//每个单词的索引
	static HashMap<String,String> wordindexmap=new HashMap<String,String>();
	//每个标签的索引
	static HashMap<String,String> tagindexmap=new HashMap<String,String>();
	//所有标示集合
	static ArrayList<String> distancelist=new ArrayList<String>();
	//每个大的分类索引
	static HashMap<String,String> categoryindexmap=new HashMap<String,String>();
	//创建停用词表
	static ArrayList<String> stopwordslist=new ArrayList<String>();
	//生成距离向量
	static HashMap<String,Integer> distancemap=new HashMap<String,Integer>();
	//
	static String searchtext=null;
	static final int  FULL_MATCH=2;
	static final int  PART_MATCH=1;
	static boolean flag=false;
	//创建索引
		public static void  createStopWordsIndex(String filename)
		{

			try {
				String line=null;
				StringBuilder sb=new StringBuilder();

			    BufferedReader br = new BufferedReader(new FileReader(filename));
			    long m=System.currentTimeMillis();
				while((line=br.readLine())!=null)
				{
					stopwordslist.add(line);
				}
				long n=System.currentTimeMillis();
			    System.out.println("时间:"+(n-m));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	//距离向量辅助函数
   public static String[] getStringArray(String text) throws Exception
   {
	   if(text.length()!=7)
	   {
		   throw new Exception("字符串错误，请联系我解决");
	   }
	   String[] array=new String[5];
	   array[0]=text.substring(0,1);
	   array[1]=text.substring(0,2);
	   array[2]=text.substring(0,4);
	   array[3]=text.substring(0,5);
	   array[4]=text.substring(0,7);
	   return array;
   }
    //创建距离索引向量
    public static void createDistanceIndex()
    {
    	for(int i=0;i<distancelist.size();i++)
    	{
    		try {
				String[] array=getStringArray(distancelist.get(i));
				for(int j=0;j<array.length;j++)
				{
					if(!distancemap.containsKey(array[j]))
					{
						distancemap.put(array[j],0);
						if(j>0)
						distancemap.put(array[j-1],distancemap.get(array[j-1])+1);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	}
    }
    //重写indexof
    public static int indexof(String text,char[] ch)
    {
    	for(int i=0;i<text.length();i++)
    	{
    		for(int j=0;j<ch.length;j++)
    		{
    			if(text.charAt(i)==ch[j])
        		{
        			return i;
        		}
    		} 		
    	}
    	return -1;
    }
    //重写split
    public static String[] split(String text,char ch)
    {
    	ArrayList<String> list=new ArrayList<String>();
    	int loc=0;
    	for(int i=0;i<text.length();i++)
    	{
    		if(text.charAt(i)==ch)
    		{
    			list.add(text.substring(loc,i));
    			loc=i+1;
    		}
    	}
		list.add(text.substring(loc,text.length()));
    	String[] res=new String[list.size()];
    	return list.toArray(res);
    }
    //创建距离列表
	public static void  createDistanceList(String filename)
	{
		char[] ch=new char[3];
		ch[0]='=';
		ch[1]='@';
		ch[2]='#';

		try {
			String line=null;
			StringBuilder sb=new StringBuilder();
		    BufferedReader br = new BufferedReader(new FileReader(filename));
			while((line=br.readLine())!=null)
			{
				int loc=indexof(line,ch);
				String s0=line.substring(0,loc);
				String s1=line.substring(loc+1,line.length());
				
				distancelist.add(s0);
		
			}
			
			long n=System.currentTimeMillis();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	//创建全局单词索引
	public static void  createIndex(String filename)
	{
		int count=0;
		char[] ch=new char[1];
		ch[0]=' ';
		long m=System.currentTimeMillis();

		try {
			String line=null;
			StringBuilder sb=new StringBuilder();
		    BufferedReader br = new BufferedReader(new FileReader(filename));
		//	char ch=' ';
			while((line=br.readLine())!=null)
			{
				int loc=indexof(line,ch);
				String s0=line.substring(0,loc);
				String s1=line.substring(loc+1,line.length());
				wordindexmap.put(s0,s1);				
			}
			
			long n=System.currentTimeMillis();
			System.out.println("索引时间为="+(n-m));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	// 创建标签索引
	public static void createTagIndex(String tags)
	{
		//String[] alltags=tags.split(",");
		char ch=',';
		String[] alltags=split(tags,ch);
		for(int i=0;i<alltags.length;i++)
		{
			tagindexmap.put(alltags[i],wordindexmap.get(alltags[i]));
		}
	}
	//将文本进行分词处理
    public static HashMap<String,String> splitWords(String content)
    {
    	searchtext=content;
    	HashMap<String,String> map=new HashMap<String,String>();
    	System.out.println("in splitwords");
	    long m=System.currentTimeMillis();
    	int len=content.length();
    	char[] array=content.toCharArray();
    	StringBuilder sb=new StringBuilder();
    	ArrayList<String> list=new ArrayList<String>();
    	for(int i=1;i<=len/2+1;i++)
    	{
    		for(int j=0;j+i<=len;j++)
    		{
    			for(int k=j;k<j+i;k++)
    			{
        			sb.append(array[k]);
    			}
    			if(wordindexmap.containsKey(sb.toString()))
    			{
    				if(!stopwordslist.contains(sb.toString().trim()))
    				{
    					list.add(sb.toString());
    					map.put(sb.toString(),wordindexmap.get(sb.toString()));
    				}
    			}
    			sb.delete(0,sb.length());
    		}
    	}
	    long n=System.currentTimeMillis();
	    System.out.println("分词时间:"+(n-m));
	    Collections.sort(list);
	    for(int i=0;i<list.size();i++)
	    {
	    	if(resultfilter(list,list.get(i)))
	    	{
	    		map.remove(list.get(i));
	    	}
	    }
	    return map;
    }
    //返回值，为最大相似度标签和相似度
    //自动补全标签内容
	//将文本进行分词处理
	public static HashMap<String,String> splitWords(String[] words)
	{
		StringBuilder content=new StringBuilder();
		for(int i=0;i<words.length;i++)
		{
			content.append(words[i]);
		}
		searchtext=content.toString();
		HashMap<String,String> map=new HashMap<String,String>();
		System.out.println("in splitwords");
		long m=System.currentTimeMillis();
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<words.length;i++)
		{
				if(wordindexmap.containsKey(words[i]))
				{
						list.add(words[i]);
						map.put(words[i],wordindexmap.get(words[i]));

				}
		}
		long n=System.currentTimeMillis();
		System.out.println("分词时间:"+(n-m));
		Collections.sort(list);
		for(int i=0;i<list.size();i++)
		{
			if(resultfilter(list,list.get(i)))
			{
				map.remove(list.get(i));
			}
		}
		return map;
	}
    public static double maxSimilarity(String s1,String s2) throws Exception
    {
    	double a=0.65;
    	double b=0.8;
    	double c=0.9;
    	double d=0.96;
    	double e=0.5;
    	double f=0.1;
    	int n=0;
    	int k=0;
    	StringBuilder sb=new StringBuilder();
    	String[] arr1=getStringArray(s1);
    	String[] arr2=getStringArray(s2);
    	if(!arr1[0].equals(arr2[0]))
    	{
    		return f;
    	}
    	sb.append(arr1[0]);
    	if(!arr1[1].equals(arr2[1]))
    	{
    		n=distancemap.get(sb.toString());
    		k=Math.abs(arr1[1].charAt(1)-arr2[1].charAt(1));
    	    double  x=(double)n*3.1415926/180;
           double m=n-k+1;
    	   return a*Math.cos(x)*m/(double)n;
    	}
    	sb.delete(0,sb.toString().length());
    	sb.append(arr1[1]);
    	if(!arr1[2].equals(arr2[2]))
    	{
    	//	System.out.println("最长前缀="+sb.toString());
    		n=distancemap.get(sb.toString());
    		k=Math.abs(Integer.parseInt(arr1[2].substring(2, 4))-Integer.parseInt(arr2[2].substring(2,4)));
    		
    	    double  x=(double)n*3.1415926/180;
            double m=n-k+1;
       //     System.out.println("m="+m+"n="+n+"k="+k+"x="+x);
    	    return b*Math.cos(x)*m/(double)n;
    	}
    	sb.delete(0,sb.toString().length());
    	sb.append(arr1[2]);
    	if(!arr1[3].equals(arr2[3]))
    	{
    		n=distancemap.get(sb.toString());
    		k=Math.abs(arr1[3].charAt(4)-arr2[3].charAt(4));
    		
    	    double  x=(double)n*3.1415926/180;
            double m=n-k+1;
    	    return c*Math.cos(x)*m/(double)n;
    	}
    	sb.delete(0,sb.toString().length());
    	sb.append(arr1[3]);
    	if(!arr1[4].equals(arr2[4]))
    	{
    	//	System.out.println("最长前缀="+sb.toString());
    		n=distancemap.get(sb.toString());
    		k=Math.abs(Integer.parseInt(arr1[4].substring(5,7))-Integer.parseInt(arr2[4].substring(5,7)));
    	    double  x=(double)n*3.1415926/180;
            double m=n-k+1;
    	    return d*Math.cos(x)*m/(double)n;
    	}
		return 1.0;
    }
     //获取标签之间的匹配程度
    public static double getTagSimilarity(String s1,String s2) throws Exception
    {
    //	String[] arr1=s1.split(",");
    //	String[] arr2=s2.split(",");
    	char ch=',';
    	String[] arr1=split(s1,ch);
       	String[] arr2=split(s2,ch);
    	double max=0;
    	for(int i=1;i<arr1.length;i++)
    	{
    		for(int j=1;j<arr2.length;j++)
    		{
    			double m=maxSimilarity(arr1[i],arr2[j]);
    			if(m>max)
    			{
    				max=m;
    			}
    		}
    	}
    	return max;
    }
    //过滤分词结果
    public static boolean resultfilter(ArrayList<String> list,String key)
    {
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).contains(key)&&!list.get(i).equals(key))
			{
				return true;
			}
		}
		return false;
    	//HashMap<String,String> 
    }
    //根据分词结果，返回标签
    public static String[]  getMostSimilarityTags(HashMap<String,String> map) throws Exception
    {
    	int arrcount=0;
    	ArrayList<String> reslist=new ArrayList<String>();
    	String[] alltags=tags.split(",");
    	//首先每个标签的初始值是0，存放在一个hashmap中
    	HashMap<String,Double> tagssimilaritymap=new HashMap<String,Double>();
    	for(int i=0;i<alltags.length;i++)
    	{
    		tagssimilaritymap.put(alltags[i],0.0);
    	}
      //  System.out.println("在获取最大标签中"+alltags.length);

    	Set<Map.Entry<String,String>> set=  map.entrySet();
	    Iterator<Map.Entry<String,String>> it=set.iterator();
	    //首先取出分词结果
	    while(it.hasNext())
	    {
	    	Map.Entry<String, String> entry=it.next();
	    	String key=entry.getKey();
	    	String value=entry.getValue();
	    	//System.out.println();
	    //	System.out.println("分词结果为"+key+"  "+value);
	    	Set<Map.Entry<String,String>> tagindexset= tagindexmap.entrySet();
		    Iterator<Map.Entry<String,String>> tagindexit=tagindexset.iterator();
		  
		    //分词结果和每个标签进行计算
		    while(tagindexit.hasNext())
		    {
		    	Map.Entry<String, String> tagindexentry=tagindexit.next();
		    	//System.out.println("标签结果为"+tagindexentry.getKey()+"  "+tagindexentry.getValue());
		    	double d=getTagSimilarity(value,tagindexentry.getValue());
		    	
		    //	System.out.println("匹配程度为:"+d);
		    	tagssimilaritymap.put(tagindexentry.getKey(),tagssimilaritymap.get(tagindexentry.getKey())+d);
		    }
	    }

	    //增加一些权值
	    for(int k=0;k<alltags.length;k++)
	    {
    		char[] ch=searchtext.toCharArray();
    		
	    	if(searchtext.contains(alltags[k]))
	    	{
	    		tagssimilaritymap.put(alltags[k],tagssimilaritymap.get(alltags[k])+FULL_MATCH);
	    	}
	    	else
	    	{
	    		for(int j=0;j<ch.length;j++)
	    		{
	    			if(inc(alltags[k],ch[j]))
	    			{
	    	    		tagssimilaritymap.put(alltags[k],tagssimilaritymap.get(alltags[k])+PART_MATCH);
	    			}
	    		}
	    	}
	    }

	    List<Map.Entry<String, Double>> infoIds =
	    	    new ArrayList<Map.Entry<String, Double>>(tagssimilaritymap.entrySet());
	  //排序
	    Collections.sort(infoIds, new Comparator<Map.Entry<String, Double>>() {   
	        public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {      
	            //return (o2.getValue() - o1.getValue()); 
	            return o1.getValue()>o2.getValue()?1:-1;
	        }
	    }); 
	    double temp=0;
	    String[] arr=new String[2];
	    for (int i=infoIds.size()-1; i>0; i--) {
	         arr=infoIds.get(i).toString().split("=");
	         if(reslist.size()==3)
	         {
	        	 break;
	         }
	         else
	         {
	        	 if(temp==0||temp-Double.parseDouble(arr[1])<0.2)
	        	 {
	     	        reslist.add(arr[0]);
	     	     //   System.out.println(arr[0]);
	    	        temp=Double.parseDouble(arr[1]);
	        	 }
	        	 /*
	        	 if(temp-Double.parseDouble(arr[1])<0.2)
	        	 {
	        		 reslist.add(arr[0]);
		    	     temp=Double.parseDouble(arr[1]);
	        	 }
	        	 */
	         }
	    }
	    String[] res=new String[reslist.size()];
	    return reslist.toArray(res);
    }
    //权值添加辅助函数
    public static boolean inc(String s,char ch)
    {
    	for(int i=0;i<s.length();i++)
    	{
    		if(s.charAt(i)==ch)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    //初入全局app
    //初始化函数，主要是索引表的建立
    public static void initStep1()
    {
    	createDistanceList("hitdicpart1.txt");
    	createDistanceList("hitdicpart2.txt");
    	createDistanceList("hitdicpart3.txt");
    }
    //初始化第二步
    public static void initStep2()
    {
		if(flag==false)
		{
			createIndex("tagindex");
			createTagIndex(tags);
			createStopWordsIndex("stopwords.txt");
			createDistanceIndex();
			flag=true;
		}
    }
	public static String[] getMostSimilarityTagsFromContent(String[] words)
	{
		HashMap<String,String> resmap= splitWords(words);
		//System.out.println(resmap);
		String[] res=null;
		try {
			res=getMostSimilarityTags(resmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	//根据输入的内容返回最匹配的标签
    public static String[] getMostSimilarityTagsFromContent(String content)
    {
    	
    	 HashMap<String,String> resmap= splitWords(content);
		 System.out.println(resmap);
    	 String[] res=null;
         try {
		res=getMostSimilarityTags(resmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return res;
    }
    //自动增加标签
    public static String[] addTags(String content,String[] tags)
    {
    	String[] res=getMostSimilarityTagsFromContent(content);
    	ArrayList<String> array=new ArrayList<String>();
    	for(int i=0;i<tags.length;i++)
    	{
    		array.add(tags[i]);
    	}
    	for(int i=0;i<res.length;i++)
    	{
    		if(!array.contains(res[i]))
    		{
    			array.add(res[i]);
    		}
    	}
    	String[] result=new String[array.size()];
    	return array.toArray(result);
    }
}
