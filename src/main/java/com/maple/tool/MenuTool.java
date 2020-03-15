package com.maple.tool;

import com.maple.common.enums.YesOrNoEnum;
import com.maple.dto.admin.MenuDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单组装工具
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-09-16 22:53
 */
public class MenuTool {
    /**
     * 顶层菜单code
     */
    private static final Long PARENT_MENU_CODE = 0L;

    /**
     * 菜单组装
     *
     * @param sourceMenuList 源菜单列表
     * @return 组装后的菜单
     */
    public static List<MenuDTO> assemblyMenu(List<MenuDTO> sourceMenuList) {
        // 将菜单根据pid分组
        Map<Long, List<MenuDTO>> menuMap = sourceMenuList.stream().collect(Collectors.groupingBy(MenuDTO::getPid,
                Collectors.toList()));
        List<MenuDTO> topMenuList = menuMap.get(PARENT_MENU_CODE);
        topMenuList.forEach(item -> assemblyChildren(menuMap, item));
        return topMenuList;
    }

    private static void assemblyChildren(Map<Long, List<MenuDTO>> menuMap, MenuDTO parentMenu) {
        // 为空或为叶子节点 则跳过
        if (Objects.isNull(parentMenu.getIsLeaf()) || YesOrNoEnum.isYes(parentMenu.getIsLeaf())) {
            return;
        }
        List<MenuDTO> childrenList = menuMap.get(parentMenu.getId());
        childrenList.forEach(item -> assemblyChildren(menuMap, item));
        parentMenu.setChildren(childrenList);
    }
}
