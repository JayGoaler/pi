package com.jaygoaler.pitools.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/20 16:41
 * <p>
 * description: 分类代码类型数据转树形结构
 */
public class TreeDataUtils {


    /**
     * 将制定集合对象转换为树形数据结构
     *
     * @param list       需要生成树形结构的数据
     * @param dmKeyName  数据代码字段名
     * @param mcKeyName  数据名称字段名
     * @param fdmKeyName 数据父级代码字段名
     * @return
     */
    public static List<NodeItem> translateTree(final List<? extends Object> list, final String dmKeyName, final String mcKeyName, final String fdmKeyName) {
        if (list == null || list.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<NodeItem> list1 = translateNodeItem(list, dmKeyName, mcKeyName, fdmKeyName);

        for (int i = 0; i < list1.size() - 1; i++) {

            NodeItem nodeItem = list1.get(i);

            // 判断当前数据是否有父级代码
            if (nodeItem.getDm() != null && nodeItem.getDm().trim().length() > 0) {

                for (int j = i + 1; j < list1.size(); j++) {

                    NodeItem nodeItem1 = list1.get(j);

                    // 判断nodeItem1是否是nodeItem1的子节点
                    if (nodeItem1.getDm() != null && nodeItem1.getDm().trim().equals(nodeItem.getDm().trim())) {

                        if (nodeItem1.getChildren() == null) {
                            nodeItem1.setChildren(new LinkedList<>());
                        }

                        // 添加子节点数据
                        nodeItem1.getChildren().add(nodeItem);

                        Collections.sort(nodeItem1.getChildren(), (o1, o2) -> o1.getDm().compareTo(o2.getDm()));

                        // nodeItem是某个节点的子节点，删除原集合中的该节点，遍历
                        list1.remove(i);
                        i = -1;
                        break;
                    }
                }
            }
        }

        return list1;
    }

    /**
     * 将原始数据对象转换为树形结构数据内部对象
     *
     * @param list
     * @param dmKeyName
     * @param mcKeyName
     * @param fdmKeyName
     * @return
     */
    private static List<NodeItem> translateNodeItem(final List<? extends Object> list, final String dmKeyName, final String mcKeyName, final String fdmKeyName) {

        return list.stream()
                .map(item -> {
                    NodeItem nodeItem = new NodeItem();
                    // 设置代码
                    Object dmValue = ObjectTools.getObjectValue(item, dmKeyName);
                    if (!Objects.isNull(dmValue)) {
                        nodeItem.setDm(String.valueOf(dmValue));
                    }
                    // 设置代码名称
                    Object mcValue = ObjectTools.getObjectValue(item, mcKeyName);
                    if (!Objects.isNull(mcValue)) {
                        nodeItem.setMc(String.valueOf(mcValue));
                    }
                    // 设置父级代码名称
                    Object fdmValue = ObjectTools.getObjectValue(item, fdmKeyName);
                    if (!Objects.isNull(fdmValue)) {
                        nodeItem.setMc(String.valueOf(fdmValue));
                    }
                    return nodeItem;
                })
                .sorted(((o1, o2) -> o2.getDm().compareTo(o1.getDm())))
                .collect(Collectors.toList());
    }


    /**
     * 树形结构数据节点对象
     */
    static class NodeItem {

        private String fdm;

        private String dm;

        private String mc;

        private List<NodeItem> children;

        public NodeItem() {
        }

        public NodeItem(String fdm, String dm, String mc) {
            this.fdm = fdm;
            this.dm = dm;
            this.mc = mc;
        }

        public String getFdm() {
            return fdm;
        }

        public void setFdm(String fdm) {
            this.fdm = fdm;
        }

        public String getDm() {
            return dm;
        }

        public void setDm(String dm) {
            this.dm = dm;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public List<NodeItem> getChildren() {
            return children;
        }

        public void setChildren(List<NodeItem> children) {
            this.children = children;
        }
    }

}
