package com.analysis.logman.FP_Growth;

import java.util.ArrayList;
import java.util.List;

/**
 * 树结构
 */
public class TreeNode implements Comparable<TreeNode>{
    private String name;//节点名称
    private Integer count;//计数
    private TreeNode parent;//父节点
    private List<TreeNode> children;//子节点
    private TreeNode nextSameChild;//下一个同名节点

    public TreeNode(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getNextSameChild() {
        return nextSameChild;
    }

    public void setNextSameChild(TreeNode nextSameChild) {
        this.nextSameChild = nextSameChild;
    }

    /**
     * 添加一个节点
     * @param child
     */
    public void addChild(TreeNode child){
        if(this.getChildren() == null){
            List<TreeNode> list = new ArrayList<TreeNode>();
            list.add(child);
            this.setChildren(list);
        }else{
            this.getChildren().add(child);
        }
    }

    /**
     * 是否存在这该节点，存在返回该节点，不存在返回空
     * @param name
     * @return
     */
    public TreeNode findChild(String name){
        List<TreeNode> children = this.getChildren();
        if (children != null){
            for (TreeNode child:children){
                if (child.getName().equals(name)){
                    return child;
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(TreeNode o) {
        int count = o.getCount();
        return count - this.count;
    }
}

