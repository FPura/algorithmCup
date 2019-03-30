package algorithmCup.data;


import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private City element;
    //private TreeNode parent;
    private int distanceFromParent;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode(City element){
        this.element = element;
    }

    public City getElement(){
        return element;
    }
    public int getDistanceFromParent(){
        return distanceFromParent;
    }
    public void addChildren(TreeNode child){
        //child.setParent(this);
        children.add(child);
        child.distanceFromParent = element.getDistances().get(child.getElement());
    }

    /*private void setParent(TreeNode parent){
        this.parent = parent;
    }*/
    /*private void setDistanceFromParent(TreeNode parent){
        this.parent = parent;
    }*/


}
