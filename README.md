
Всех приветствую! Поздравляю вас, что вы хорошо сдали ТЗ и прошли техническое интервью, теперь вы являетесь почти полноценными членами команды Android в GeekStudio. Однако, то что вы прошли до этого, еще не конец всех испытаний, которые вас ждут впереди. Я и Альберт будем испытывать вас в течении месяца и по его окончанию отберем 3 сильнейших, готовых покорять новые, неизведанные горизонты Android Development’a и TezSat.
За каждым из вас будет закреплен ментор (Тимур или Альберт), который будет следить за вашим прогрессом, ревьювить код, обсуждать проблемы и помогать.
Это будет ваше первое тестовое задание, по выполнении которого вы ознакомитесь со всем тем, что вам понадобится при работе над TezSat. 
После того как вы приступили к тестовому заданию, пишите вашему куратору время начала выполнения таска. Все проблемы и вопросы пишите ему.

**<p align="center">Test Task #1
Multi-module project introduction
Kotlin DSL ( BuildSrc ) 
Powerful .gitignore
Advanced Mapping
Advanced Kotlin Coroutines + Flow Usage</p>**

Все тестовые задания будут выполняться в одном проекте, использующий Kitsu API как бэкэнд. По ходу выполнения тестовых заданий, приложение будет наполняться новым функционалом. По окончанию всех тестовых заданий, вы будете готовы покорять Tez Sat.

** Необходимо создать приложение, реализовав следующее : 
    - Экран с Tab Layout, состоящий из 3 табов : 
                    -  RecyclerView с Аниме
                    - RecyclerView с Мангой
                    - RecyclerView с Пользователями                                                                                                                                                      
    - Дизайн брать с Kitsu приложения**

Требования по используемым технологиям: 
 - Kotlin + Kotlin code style 
 - Single Activity + Clean Architecture + MVVM
 - Koin 
 - Modularization
 - Retrofit + OkHTTP
 - Paging 3 
 - Kotlin DSL (BuildSrc)
 - Powerful .gitignore
 - Kotlin coroutines + Flow
 - ViewPager2
 - Navigation Component
 - Material Design ( Material Components only )
**![](https://lh4.googleusercontent.com/hS2EL300U9ZcL83YJmexY_p-aU42N6Ai5IP63hvlJLDrrbVAeyqFU5nAksR4GyPt94YHSaB9fHaKESlOyWcICZ9gU7nfj45zgL_lo0TeWCibWbwibVjq0QYToBSaVhxLdPsy7MUNcsU4YB_xHYPEvRk)
Говоря Powerful .gitignore я подразумеваю используя следующее для root .gitignore. Значит, при пуше проекта, папки .idea не будет
```
../.gitignore

### Android template
# Built application files
*.apk
*.aar
*.ap_
*.aab

# Files for the ART/Dalvik VM
*.dex

# Java class files
*.class

# Generated files
bin/
gen/
out/
#  Uncomment the following line in case you need and you don't have the release build type files in your app
# release/

# Gradle files
.gradle/
build/

# Local configuration file (sdk path, etc)
local.properties

# Proguard folder generated by Eclipse
proguard/

# Log Files
*.log

# Android Studio Navigation editor temp files
.navigation/

# Android Studio captures folder
captures/

# IntelliJ
/.idea
*.iml
.idea/workspace.xml
.idea/tasks.xml
.idea/gradle.xml
.idea/assetWizardSettings.xml
.idea/dictionaries
.idea/libraries
# Android Studio 3 in .gitignore file.
.idea/caches
.idea/modules.xml
# Comment next line if keeping position of elements in Navigation Editor is relevant for you
.idea/navEditor.xml

# Keystore files
# Uncomment the following lines if you do not want to check your keystore files in.
#*.jks
#*.keystore

# External native build folder generated in Android Studio 2.2 and later
.externalNativeBuild
.cxx/

# Google Services (e.g. APIs or Firebase)
# google-services.json

# Freeline
freeline.py
freeline/
freeline_project_description.json

# fastlane
fastlane/report.xml
fastlane/Preview.html
fastlane/screenshots
fastlane/test_output
fastlane/readme.md

# Version control
vcs.xml

# lint
lint/intermediates/
lint/generated/
lint/outputs/
lint/tmp/
# lint/reports/

# Android Profiling
*.hprof

#MacOSX
**/.DS_Store
```

После выполнения таска, заливаете на GitHub и отправляете вашему прикрепленному куратору
