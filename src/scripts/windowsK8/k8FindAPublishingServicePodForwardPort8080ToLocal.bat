@echo off
setlocal enabledelayedexpansion
echo Searching for pods with "publishing-service-" in name. Will forward port 8080 to local of first one in "Running" status

for /f "tokens=1,2,3,4,5" %%a in ('kubectl get pods ^| findstr "^publishing-service-"') do (

	echo "Evaluating pod %%a with ready containers %%b; pod status %%c, restarts %%d, age %%e "

    if "%%c"=="Running" (
        set POD_NAME=%%a
        goto :forward
    )
)

:forward
if not defined POD_NAME (
    echo No running pods found starting with "publishing-service-".
    exit /b 1
)

echo Forwarding pod !POD_NAME! on port 8080. Press Ctrl-C to stop
kubectl port-forward !POD_NAME! 8080:8080