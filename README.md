# 📝 To Do List App

<img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/> <img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white"/> <img src="https://img.shields.io/badge/Material-Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white"/>

**To Do List App**은 Android(Kotlin) 기반의 간단한 할 일 관리 애플리케이션입니다.
사용자는 날짜, 카테고리별로 할 일을 추가·관리·삭제할 수 있고, 체크박스로 완료 상태 표시가 가능합니다.

---

## 🔑 주요 기능

* **할 일 추가 & 편집**

  * 제목, 설명, 날짜, 카테고리(공부/운동/업무/기타)를 입력해 저장
* **체크리스트 완료 표시**

  * 완료 시 텍스트에 취소선 표시
  * DB에 즉시 저장되어 앱 재시작·탭 이동 시에도 상태 유지
* **카테고리 필터링**

  * Spinner 힌트(“카테고리”) 포함, 선택 전 전체 목록 표시
  * 선택 후 해당 카테고리의 할 일만 필터링
* **달력 뷰 & 이벤트 표시**

  * MaterialCalendarView로 달력 표시
  * 할 일 존재일에 점(Dot) 데코레이션
  * 날짜 클릭 시 해당일 할 일 목록 표시 & 새 할 일 추가 화면으로 이동
* **Swipe to Delete**

  * 좌/우 스와이프로 할 일 삭제
  * 롱 클릭으로도 삭제 가능

---

## 🏗️ 아키텍처 & 디렉토리 구조

```
To_Do_List_Project/
├─ app/
│  ├─ src/main/
│  │  ├─ java/com/example/todolist/
│  │  │  ├─ AddTaskActivity.kt      # 할 일 추가/편집 화면
│  │  │  ├─ MainActivity.kt         # BottomNavigation + 프래그먼트 교체
│  │  │  ├─ data/                   
│  │  │  │  └─ TaskDBHelper.kt      # SQLiteOpenHelper CRUD
│  │  │  ├─ fragments/              
│  │  │  │  ├─ ListFragment.kt      # 할 일 목록 + 필터링
│  │  │  │  └─ CalendarFragment.kt  # 달력 뷰 + 날짜별 조회
│  │  │  └─ adapter/                 
│  │  │     └─ TaskAdapter.kt       # RecyclerView Adapter
│  │  └─ res/
│  │     ├─ layout/                  # XML 레이아웃 파일
│  │     ├─ values/                  # colors.xml, strings.xml
│  │     └─ drawable/                # 버튼·배경 리소스
└─ build.gradle                      # 프로젝트 설정
```

---

## 🚀 빠른 시작

1. **리포지터리 클론**

   ```bash
   git clone https://github.com/aeeun-git/To_Do_List_App_Project.git
   cd To_Do_List_App_Project
   ```

2. **Android Studio로 열기**

   * 'Open an existing Android Studio project' → 프로젝트 루트 선택

3. **앱 빌드 & 실행**

   * 에뮬레이터 또는 실기기 연결 후 ▶️ Run

4. **앱 사용하기**

   * 하단 탭으로 **할 일 목록**, **달력** 전환
   * **+ 버튼** 눌러 새 할 일 추가
   * 체크/스와이프/카테고리 필터링 등 기능 체험

---

## 📦 의존성

* Kotlin Standard Library
* AndroidX Core, AppCompat, Material Components
* RecyclerView, ConstraintLayout
* MaterialCalendarView (3rd-party)

---

## 📝 라이선스

MIT © [aeeun-git](https://github.com/aeeun-git)

---
