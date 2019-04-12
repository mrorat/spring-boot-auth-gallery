rd /s /q "target"
call mvn package -DskipTests=true
dir /b target\*.jar > target\jar_file.txt
set /P myJar=<target\jar_file.txt
echo %myJar%
c:\Software\putty\pscp.exe -i c:\Users\a\.ssh\dell_id_rsa_new.ppk target\%myJar% michal@dell:/home/michal
c:\Software\putty\plink.exe michal@dell -i c:\Users\a\.ssh\dell_id_rsa_new.ppk "sudo /home/michal/deploy_gallery.sh %myJar%"
