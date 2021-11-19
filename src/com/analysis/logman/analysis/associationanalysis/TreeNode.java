package com.analysis.logman.analysis.associationanalysis;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: FP树的节点
 */
class TreeNode {
    /**节点名称**/
    private String name;
    /**频数**/
    private int count;
    private TreeNode parent;
    private List<TreeNode> children;
    /**下一个节点（由表头项维护的那个链表）**/
    private TreeNode nextHomonym;
    /**末节点（由表头项维护的那个链表）**/
    private TreeNode tail;

    @Override
    public String toString() {
        return name;
    }

    public TreeNode() {
    }

    public TreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public void addChild(TreeNode child) {
        if (getChildren() == null) {
            List<TreeNode> list = new ArrayList<TreeNode>();
            list.add(child);
            setChildren(list);
        } else {
            getChildren().add(child);
        }
    }

    public TreeNode findChild(String name) {
        List<TreeNode> children = getChildren();
        if (children != null) {
            for (TreeNode child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void printChildrenName() {
        List<TreeNode> children = getChildren();
        if (children != null) {
            for (TreeNode child : children)
                System.out.print(child.getName() + " ");
        } else
            System.out.print("null");
    }

    public TreeNode getNextHomonym() {
        return this.nextHomonym;
    }

    public void setNextHomonym(TreeNode nextHomonym) {
        this.nextHomonym = nextHomonym;
    }

    public void countIncrement(int n) {
        this.count += n;
    }

    public TreeNode getTail() {
        return tail;
    }

    public void setTail(TreeNode tail) {
        this.tail = tail;
    }

}
