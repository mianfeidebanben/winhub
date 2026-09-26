package com.winlator.star.ui

sealed class Screen(val route: String, val label: String, val iconName: String) {
    object Containers    : Screen("containers",     "容器",             "folder")
    object Shortcuts     : Screen("shortcuts",      "快捷方式",              "shortcut")
    object Contents      : Screen("contents",       "附加内容",               "inventory_2")
    object InputControls : Screen("input_controls", "输入控件",         "sports_esports")
    object AdrenoTools   : Screen("adreno_tools",   "Adrenotools GPU 驱动","memory")
    object Saves         : Screen("saves",          "存档",                  "save")
    object FileManager   : Screen("file_manager",   "文件管理器",           "folder_open")
    object Settings      : Screen("settings",       "设置",               "settings")
    object Appearance    : Screen("appearance",     "外观",             "palette")
    object LsfgSettings  : Screen("lsfg_settings",  "Vegas 帧生成",           "video_settings")

    object Gog    : Screen("gog",    "GOG",          "storefront")
    object Epic   : Screen("epic",   "Epic Games",   "storefront")
    object Amazon : Screen("amazon", "Amazon Games", "storefront")
    object Steam  : Screen("steam",  "Steam",        "storefront")

    object ContainerDetail : Screen("container_detail?id={id}", "Container", "")

    companion object {
        val drawerItems by lazy {
            listOf(Shortcuts, Containers, Settings, Appearance, InputControls, AdrenoTools, LsfgSettings, Saves)
        }
        val storeItems by lazy {
            listOf(Gog, Epic, Amazon, Steam)
        }
    }
}
