# SimpleAndroidCalculator

## 当前状态
- 项目代码已生成完成。
- 本机当前缺少 Android 构建工具链（`gradlew`/Gradle 与 Android SDK），所以无法在此终端直接产出 APK。

## 一步生成 APK（不装 Android Studio）
已内置 GitHub Actions 工作流：`.github/workflows/build-apk.yml`

操作：
1. 把此项目推到 GitHub 仓库（分支建议 `main`）。
2. 打开 GitHub 仓库 -> `Actions` -> `Build Android APK`。
3. 点击 `Run workflow`。
4. 构建完成后，在该次运行页面的 `Artifacts` 下载：
   - `simple-android-calculator-debug-apk`
5. 解压后得到：
   - `app-debug.apk`

## 你本机最快出 APK 的方法（Android Studio）
1. 安装 Android Studio（如果还没装）。
2. 打开项目目录：`/Users/xiaofengwui/Documents/Mac_app/SimpleAndroidCalculator`
3. 等待 Gradle Sync 完成（首次会自动下载 Gradle 和 Android 依赖）。
4. 菜单点击：`Build -> Build APK(s)`。
5. 生成路径：`app/build/outputs/apk/debug/app-debug.apk`

## 功能
- 加减乘除
- 小数点
- 百分比
- 正负号
- AC 清空
