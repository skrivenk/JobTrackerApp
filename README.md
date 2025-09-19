#  Java Job Application Tracker

A Java Swing-based desktop application to help job seekers track, manage, and analyze their job applications. The app uses an embedded H2 database and offers features like search, edit, report generation, and CSV export, all in a clean, multi-panel GUI.

---

##  Features

-  **Add & Edit Applications**: Track job title, company, date, status, and notes.
-  **Dashboard View**: View all job applications in a sortable table.
-  **Search Panel**: Filter applications by keywords.
-  **Reports Panel**: Generate visual bar chart reports (with JFreeChart) and statistics on applications.
-  **Local Database**: Uses embedded H2 database (stored in `C:/ProgramData/JobTrackerApp/`).
-  **CSV Export**: Export your application data to CSV files.
-  **Database Test Utility**: Validate DB connection with a simple test class.

---

## 🛠 Technologies Used

- **Java 8+**
- **Swing GUI Toolkit**
- **H2 Embedded Database**
- **JFreeChart** (for data visualization)
- **JDateChooser** (for date selection)

---

##  Project Structure

```
src/
├── MainApp.java             # Entry point with UI setup and navigation
├── DashboardPanel.java      # Displays applications in a table
├── AddEditPanel.java        # Form to add/edit applications
├── SearchPanel.java         # Search and filter functionality
├── ReportsPanel.java        # Generates summary + bar chart reports
├── JobApplication.java      # POJO model for job application data
├── JobApplicationDAO.java   # Handles all DB CRUD operations
├── DatabaseConnection.java  # Connects to embedded H2 DB
└── TestDBConnection.java    # Simple class to verify DB connection
```

---

##  Getting Started

1. **Clone this repo**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/java-job-tracker.git
   ```

2. **Open in your IDE (e.g. IntelliJ, NetBeans)** and build the project.

3. **Ensure required libraries are available**:
   - Add **H2 Database JAR**
   - Add **JFreeChart JAR**
   - Add **JCalendar / JDateChooser JAR**

4. **Run `MainApp.java`** to launch the GUI.

---

##  Database Info

- Location: `C:/ProgramData/JobTrackerApp/job_tracker.mv.db`
- Default user: `sa`
- Password: *(empty)*

---

##  Test

Run `TestDBConnection.java` to ensure the database is set up and accessible.

---

##  Author

**Sergei Krivenkov**  
[LinkedIn](https://www.linkedin.com/in/skrivenkov) • [Portfolio](https://sergeik.com)
