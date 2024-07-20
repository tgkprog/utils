@echo off
setlocal enabledelayedexpansion
echo Searching for pods with "publishing-service-" in name 

for /f "tokens=1,2,3,4,5" %%a in ('kubectl get pods ^| findstr "^publishing-service-"') do (

	echo "Pod %%a with ready containers %%b; pod status %%c, restarts %%d, age %%e "
)
