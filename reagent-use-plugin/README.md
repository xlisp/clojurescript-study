# Reagent使用第三方jq插件

* 一. Reagent 调用第三方jq插件jquery-date-range-picker实例
* 二. Reagent 调用第三方jq插件highcharts实例
* 三. Reagent 其他非jq插件wordcloud插件

### 一. Reagent 调用第三方jq插件jquery-date-range-picker实例

* 本项目用第三方的jquery-date-range-picker

#### 1. 生产环境:

```bash
➜  my-externs git:(master) ✗ lein do clean, cljsbuild once min

Compiling ClojureScript...
Compiling "resources/public/js/compiled/my_externs.js" from ["src"]...
Successfully compiled "resources/public/js/compiled/my_externs.js" in 19.851 seconds.

➜  my-externs git:(master) ✗

```
![](./date-range-picker.png)

#### 2. 开发环境

```bash

➜  my-externs git:(master) ✗ lein figwheel
Figwheel: Cutting some fruit, just a sec ...
Figwheel: Validating the configuration found in project.clj
Figwheel: Configuration Valid :)
Figwheel: Starting server at http://0.0.0.0:3449
Figwheel: Watching build - dev
Figwheel: Cleaning build - dev
Compiling "resources/public/js/compiled/my_externs.js" from ["src"]...
Successfully compiled "resources/public/js/compiled/my_externs.js" in 22.532 seconds.
Figwheel: Starting CSS Watcher for paths  ["resources/public/css"]
Launching ClojureScript REPL for build: dev
Figwheel Controls:
          (stop-autobuild)                ;; stops Figwheel autobuilder
          (start-autobuild [id ...])      ;; starts autobuilder focused on optional ids
          (switch-to-build id ...)        ;; switches autobuilder to different build
          (reset-autobuild)               ;; stops, cleans, and starts autobuilder
          (reload-config)                 ;; reloads build config and resets autobuild
          (build-once [id ...])           ;; builds source one time
          (clean-builds [id ..])          ;; deletes compiled cljs target files
          (print-config [id ...])         ;; prints out build configurations
          (fig-status)                    ;; displays current state of system
  Switch REPL build focus:
          :cljs/quit                      ;; allows you to switch REPL to another build
    Docs: (doc function-name-here)
    Exit: Control+C or :cljs/quit
 Results: Stored in vars *1, *2, *3, *e holds last exception object
Prompt will show when Figwheel connects to your application
To quit, type: :cljs/quit
cljs.user=>

```

### 二. Reagent 调用第三方jq插件highcharts实例

