@echo off
if not exist "test\output" mkdir "test\output"
set PASS=0
set FAIL=0
for %%f in (test\input\*.txt) do (
    (echo load %%~nxf && echo save %%~nxf && echo reset && echo quit) | java -cp target\classes model.Prototipus > nul 2>&1
    fc "test\expected\%%~nf_elvart.txt" "test\output\%%~nxf" > nul 2>&1
    if errorlevel 1 (
        echo FAIL: %%~nxf
        set /a FAIL+=1
    ) else (
        set /a PASS+=1
    )
)
set /a TOTAL=%PASS%+%FAIL%
echo.
echo Eredmeny: %PASS% / %TOTAL% teszt sikeres
if %FAIL% gtr 0 echo Sikertelen tesztek szama: %FAIL%
