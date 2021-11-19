package com.analysis.logman.FP_Growth;

import com.analysis.logman.dao.AnalysisDao;
import com.analysis.logman.entity.FwlogEntity;
import com.analysis.logman.entity.SrcDest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Myfptree {
    public static final int support = 10;//设定最小支持频次为2
    //保存第一次的次序
    public Map<String,Integer> ordermap = new HashMap<String,Integer>();

    private AnalysisDao analysisDao = new AnalysisDao();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public List<String> treeNode = new LinkedList<>();
    public LinkedList<LinkedList<String>> readTransaction(Date st,Date et) throws IOException{

//      原始的读文件的操作
//        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
//        String filePath="E:\\log\\useritems.csv";
//        BufferedReader br = new BufferedReader(new InputStreamReader(
//                new FileInputStream(filePath)
//        ));
//        for (String line = br.readLine();line != null;line = br.readLine()){
//            if (line.length() == 0 || "".equals(line)) continue;
//            String[] str = line.split(",");
//            LinkedList<String> limt = new LinkedList<String>();
//            for (int i= 0 ; i < str.length ; i++){
//                limt.add(str[i].trim());
//            }
//            records.add(limt);
//        }
//        br.close();
//        return records;

        //从数据库中读取内容进行操作
        List<FwlogEntity> srcDests = analysisDao.findFwlogByTime(st,et);
        LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
        for (FwlogEntity object:srcDests){
            SrcDest srcDest = new SrcDest();
            srcDest.setSrcip(object.getSrcip());
            srcDest.setDestip(object.getDestip());
            srcDest.setDestport(object.getDestport());
            if (srcDest.toString().length()==0 || srcDest.toString().equals("")) continue;
            String[] str = srcDest.toString().split(",");
            LinkedList<String> list = new LinkedList<>();
            for (int i = 0 ; i < str.length ; i++){
                list.add(str[i].trim());
            }
            records.add(list);
        }
        return records;
    }

//    创建表头链
    public LinkedList<TreeNode> buildHeaderLink(LinkedList<LinkedList<String>> records){
        LinkedList<TreeNode> header = null;
        if (records.size() > 0){
            header = new LinkedList<TreeNode>();
        }else{
            return null;
        }
        Map<String,TreeNode> map = new HashMap<>();
        for (LinkedList<String> items:records){
            for (String item:items){
                //如果存在数量增1，不存在则新增
                if (map.containsKey(item)){
                    map.get(item).setCount(map.get(item).getCount() + 1);
                }else{
                    TreeNode node = new TreeNode();
                    node.setName(item);
                    node.setCount(1);
                    map.put(item,node);
                }
            }
        }
        //把支持度大于（或等于）minSup的项，加入到F1中
        Set<String> names = map.keySet();
        for (String name:names){
            TreeNode tnode = map.get(name);
            if (tnode.getCount() >= support){
                header.add(tnode);
            }
        }
        sort(header);
        return header;
    }
    //选择排序法，如果次数相等，则按名字排序，字典顺序，先小写后大写
    public List<TreeNode> sort(List<TreeNode> list){
        int len = list.size();
        for (int i = 0 ; i < len ; i ++){
            for (int j = i+1;j<len;j++){
                TreeNode node1 = list.get(i);
                TreeNode node2 = list.get(j);
                if (node1.getCount() < node2.getCount()){
                    TreeNode temp = new TreeNode();
                    temp = node2;
                    list.remove(j);
//                  list指定位置插入，原来的>=j元素都会下移，不会删除，所以插入前要删除掉原来的元素
                    list.add(j,node1);
                    list.remove(i);
                    list.add(i,temp);
                }
//                若果次数相等，则按名字排序，字典顺序，先小写后大写
                if (node1.getCount() == node2.getCount()){
                    String name1 = node1.getName();
                    String name2 = node2.getName();
                    int flag = name1.compareTo(name2);
                    if (flag > 0){
                        TreeNode tmp = new TreeNode();
                        tmp = node2;
                        list.remove(j);

                        list.add(j,node1);
                        list.remove(i);
                        list.add(i,tmp);
                    }
                }
            }
        }
        return list;
    }

    //选择排序，降序，如果同名按L中的次序排序
    public   List<String> itemsort(LinkedList<String> lis,List<TreeNode> header){
        //List<String> list=new ArrayList<String>();
        //选择法排序
        int len=lis.size();
        for(int i=0;i<len;i++){
            for(int j=i+1;j<len;j++){
                String key1=lis.get(i);
                String key2=lis.get(j);
                Integer value1=findcountByname(key1,header);
                if(value1==-1)continue;
                Integer value2=findcountByname(key2,header);
                if(value2==-1)continue;
                if(value1<value2){
                    String tmp=key2;
                    lis.remove(j);
                    lis.add(j,key1);
                    lis.remove(i);
                    lis.add(i,tmp);
                }
                if(value1==value2){
                    int v1=ordermap.get(key1);
                    int v2=ordermap.get(key2);
                    if(v1>v2){
                        String tmp=key2;
                        lis.remove(j);
                        lis.add(j,key1);
                        lis.remove(i);
                        lis.add(i,tmp);
                    }
                }
            }
        }
        return lis;
    }
    public Integer findcountByname(String itemname,List<TreeNode> header){
        Integer count=-1;
        for(TreeNode node:header){
            if(node.getName().equals(itemname)){
                count= node.getCount();
            }
        }
        return count;
    }

    /**
     *
     * @param records 构建树的记录,如I1,I2,I3
     * @param header 书中介绍的表头
     * @return 返回构建好的树
     */
    public TreeNode builderFpTree(LinkedList<LinkedList<String>> records,List<TreeNode> header){

        TreeNode root;
        if(records.size()<=0){
            return null;
        }
        root=new TreeNode();
        for(LinkedList<String> items:records){
            //itemsort(items,header);
            addNode(root,items,header);
        }

        return root;
    }
    //当已经有分枝存在的时候，判断新来的节点是否属于该分枝的某个节点，或全部重合，递归
    public  TreeNode addNode(TreeNode root,LinkedList<String> items,List<TreeNode> header){
        if(items.size()<=0) return null;
        String item=items.poll();
        //当前节点的孩子节点不包含该节点，那么另外创建一支分支。
        TreeNode node=root.findChild(item);
        if(node == null){
            node=new TreeNode();
            node.setName(item);
            node.setCount(1);
            node.setParent(root);
            root.addChild(node);

            //加将各个节点加到链头中
            for(TreeNode head:header){
                if(head.getName().equals(item)){
                    while(head.getNextSameChild()!=null){
                        head=head.getNextSameChild();
                    }
                    head.setNextSameChild(node);
                    break;
                }
            }
            //加将各个节点加到链头中
        }else{
            node.setCount(node.getCount()+1);
        }

        addNode(node,items,header);
        return root;
    }
    //从叶子找到根节点，递归之
    public void toroot(TreeNode node,LinkedList<String> newrecord){
        if(node.getParent()==null) return;
        String name=node.getName();
        newrecord.add(name);
        toroot(node.getParent(),newrecord);
    }
    //对条件FP-tree树进行组合，以求出频繁项集
    public void combineItem(TreeNode node,LinkedList<String> newrecord,String Item){
        if(node.getParent()==null)return;
        String name=node.getName();
        newrecord.add(name);
        toroot(node.getParent(),newrecord);
    }
    //fp-growth
    public List<String> fpgrowth(LinkedList<LinkedList<String>> records,String item){
        //保存新的条件模式基的各个记录，以重新构造FP-tree
        LinkedList<LinkedList<String>> newrecords=new LinkedList<LinkedList<String>>();
        //构建链头
        LinkedList<TreeNode> header=buildHeaderLink(records);
        //创建FP-Tree
        TreeNode fptree= builderFpTree(records,header);
        //结束递归的条件
        if(header.size()<=0||fptree==null){
            return treeNode;
        }
        //打印结果,输出频繁项集
        if(item!=null){
            //寻找条件模式基,从链尾开始
            for(int i=header.size()-1;i>=0;i--){
                TreeNode head=header.get(i);
                String itemname=head.getName();
                Integer count=0;
                while(head.getNextSameChild()!=null){
                    head=head.getNextSameChild();
                    //叶子count等于多少，就算多少条记录
                    count=count+head.getCount();

                }
                //打印频繁项集
                String ordinaryItem = head.getName() + "," + item + "\t" + count;
                treeNode.add(ordinaryItem);
//                System.out.println(ordinaryItem);
            }
        }
        //寻找条件模式基,从链尾开始
        for(int i=header.size()-1;i>=0;i--){
            TreeNode head=header.get(i);
            String itemname;
            //再组合
            if(item==null){
                itemname=head.getName();
            }else{
                itemname=head.getName()+","+item;
            }

            while(head.getNextSameChild()!=null){
                head=head.getNextSameChild();
                //叶子count等于多少，就算多少条记录
                Integer count=head.getCount();
                for(int n=0;n<count;n++){
                    LinkedList<String> record=new LinkedList<String>();
                    toroot(head.getParent(),record);
                    newrecords.add(record);
                }
            }
            //System.out.println("-----------------");
            //递归之,以求子FP-Tree
            fpgrowth(newrecords,itemname);
        }
        return treeNode;
    }
    //保存次序，此步也可以省略，为了减少再加工结果的麻烦而加
    public void orderF1(LinkedList<TreeNode> orderheader){
        for(int i=0;i<orderheader.size();i++){
            TreeNode node=orderheader.get(i);
            ordermap.put(node.getName(), i);
        }
    }

    public static void main(String[] args) throws IOException,ParseException {
        // TODO Auto-generated method stub
        Long st = System.currentTimeMillis();
        //读取数据

        String startTime = "2016-02-19 00:00:00";
        String endTime = "2016-02-19 00:01:02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Myfptree fpg=new Myfptree();
        LinkedList<LinkedList<String>> records=fpg.readTransaction(sdf.parse(startTime),sdf.parse(endTime));
        LinkedList<TreeNode> orderheader=fpg.buildHeaderLink(records);
        fpg.orderF1(orderheader);
        List<String> result = fpg.fpgrowth(records,null);

        for (int i = 0 ; i < result.size() ; i++)
            System.out.println(result.get(i));
        Long et = System.currentTimeMillis();
        System.out.println(et-st);
    }
}
