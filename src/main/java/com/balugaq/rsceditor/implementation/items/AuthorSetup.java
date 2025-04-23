package com.balugaq.rsceditor.implementation.items;

import com.balugaq.rsceditor.implementation.items.author.AuthorInfo;
import com.balugaq.rsceditor.implementation.items.author.UsageInfo;
import com.balugaq.rsceditor.utils.SlimefunItemUtil;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class AuthorSetup {
    public static void register() {
        AuthorInfo authorInfo = new AuthorInfo(
                new SlimefunItemStack(
                        "RSC_EDITOR_AUTHOR_INFO",
                        new CustomItemStack(
                                Material.PAPER,
                                "&b版本信息",
                                "&b作者: balugaq",
                                "&b版本: 44"
                        )
                )
        );
        SlimefunItemUtil.registerItem(authorInfo);

        UsageInfo usageInfo = new UsageInfo(
                new SlimefunItemStack(
                        "RSC_EDITOR_USAGE_INFO",
                        new CustomItemStack(
                                Material.PAPER,
                                "&bRSC 编辑器使用说明",
                                "&b使用 \"构造器\" 开始生成配置文件",
                                "&b输入 \"rhelp\" 开始编辑自定义物品"
                        )
                )
        );
        SlimefunItemUtil.registerItem(usageInfo);
    }
}
