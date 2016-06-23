package org.teiid.test.teiid3617;

import java.util.ArrayList;
import java.util.List;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

public class ModelNodeTest {

    public static void main(String[] args) {
        
        String attrValue = "[(\"user1\" => \"25\"),(\"user2\" => \"35\")]";
        attrValue = attrValue.replace("[", "").replace("]", "").replace("(", "").replace(")", "").replace("\"", "");
        ModelNode node = new ModelNode(ModelType.LIST);
        List<ModelNode> collections = new ArrayList<>();
        for(String prop : attrValue.split(",")){
            String[] array = prop.split("=>");
            String key = array[0].trim();
            String value = array[1].trim();
            ModelNode propNode = new ModelNode(ModelType.PROPERTY);
            propNode.set(key, Long.parseLong(value));
            collections.add(propNode);
        }
        node.set(collections);
        System.out.println(node.toJSONString(true));
        List<Property> list = node.asPropertyList();
        for(Property prop : node.asPropertyList()){
            System.out.println(prop.getName() + " -> " + prop.getValue().asLong());
        }
        
    }

}
