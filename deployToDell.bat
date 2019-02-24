rd /s /q "target"
call mvn package -DskipTests=true
dir /b target\*.jar > target\jar_file.txt
set /P myJar=<target\jar_file.txt
echo %myJar%
c:\Software\putty\pscp.exe -pw CiekaweHasloMichal target\%myJar% michal@dell:/home/michal
c:\Software\putty\plink.exe michal@dell -pw CiekaweHasloMichal "sudo /home/michal/deploy_gallery.sh %myJar%"