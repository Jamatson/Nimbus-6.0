# 🌥️ Nimbus — Phone Setup Guide

## All files are in ONE flat folder — easy to upload from your phone!

Files with `__` in the name represent folder paths.
Example: `app__src__main__java__com__nimbus__MainActivity.kt`
       = `app/src/main/java/com/nimbus/MainActivity.kt`

The GitHub Action automatically reorganizes them before building!

---

## 📱 Step-by-Step (Phone Only)

### STEP 1 — Create a GitHub account
Go to github.com → Sign up (free)

### STEP 2 — Create a new repo
Tap + → New repository → Name: nimbus-app → Public → Create repository

### STEP 3 — Create the workflow file (one manual path needed)
1. On your repo, tap "creating a new file"
2. In the name box type exactly:  .github/workflows/build-apk.yml
   (GitHub creates the folders automatically as you type /)
3. Paste the full contents of build-apk.yml from this folder
4. Tap Commit changes

### STEP 4 — Upload all other files
1. Tap Add file → Upload files
2. Select ALL files except build-apk.yml and README.md
3. Commit changes ✅

### STEP 5 — Get your APK
1. Tap the Actions tab
2. Wait ~8-10 minutes for the green checkmark ✅
3. Tap the completed run → scroll to Artifacts
4. Download nimbus-debug-apk

### STEP 6 — Install it
Settings → Apps → Special app access → Install unknown apps
→ allow your browser → tap the APK → Install → Open Nimbus 🌥️
