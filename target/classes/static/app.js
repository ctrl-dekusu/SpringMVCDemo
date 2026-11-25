const API_URL = "http://localhost:8080/api/students";
const API_AUTH = "http://localhost:8080/api/auth";

/**
 * Creates a new student record.
 *
 * @async
 * @param {Object} studentData - The student details to create.
 * @param {string} studentData.firstName - The student's first name.
 * @param {string} studentData.lastName - The student's last name.
 * @param {string} studentData.email - The student's email address.
 * @returns {Promise<void>} Resolves after the create request completes.
 */
async function createStudent(studentData) {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(studentData),
      credentials: "include"
    });

    if (response.ok) {
      alert("Student added successfully!");
      resetForm();
      getAllStudents();
      window.location.href = "index.html";
    } else {
      alert("Error adding student.");
    }
  } catch (error) {
    console.error("Error creating student: ", error);
  }
}

document.getElementById("studentForm").addEventListener("submit", function(e) {
  e.preventDefault();
  
  if (!validateStudentForm()) {
    return;
  }

  const id = document.getElementById("studentId").value;
  const studentData = {
    firstName: document.getElementById("firstName").value.trim(),
    lastName: document.getElementById("lastName").value.trim(),
    email: document.getElementById("email").value.trim()
  };

  if (id) {
    updateStudent(id, studentData);
  } else {
    createStudent(studentData);
  }
});

/**
 * Clears the student form and restores the submit button to add mode.
 *
 * @returns {void}
 */
function resetForm() {
  document.getElementById("studentId").value = "";
  document.getElementById("studentForm").reset();

  const submitBtn = document.getElementById("submitBtn");
  submitBtn.textContent = "Add Student";
  submitBtn.style.backgroundColor = "#28a745";
}

/**
 * Fetches all students and renders them in the student table.
 *
 * @async
 * @returns {Promise<void>} Resolves after the table is populated.
 */
async function getAllStudents() {
  try {
    const response = await fetch(API_URL, {
      credentials: "include"
    });
    if (!response.ok) throw new Error("Network response was not ok");

    const students = await response.json();
    const tableBody = document.getElementById("studentTableBody");
    tableBody.innerHTML = "";

    students.forEach(student => {
      const row = `
        <tr>
          <td>${student.id}</td>
          <td>${student.firstName}</td>
          <td>${student.lastName}</td>
          <td>${student.email}</td>
          <td>
            <button class="btn-edit" 
            onclick="initializeStudentFormById(${student.id})">
              Edit
            </button>
            <button class="btn-delete" 
            onclick="deleteStudent(${student.id})">
              Delete
            </button>
          </td>
        </tr>
      `;
      tableBody.innerHTML += row;
    });
  } catch (error) {
    console.error("Error fetching students: ", error);
    alert("Failed to fetch students. Is your Spring Boot backend running?");
  }
}

document.addEventListener("DOMContentLoaded", getAllStudents);

/**
 * Loads a student's details into the form for editing.
 *
 * @async
 * @param {number|string} id - The identifier of the student to edit.
 * @returns {Promise<void>} Resolves after the form is initialized.
 */
async function initializeStudentFormById(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      credentials: "include"
    });
    if (!response.ok) throw new Error("Could not fetch student details.");

    const student = await response.json();

    document.getElementById("studentId").value = student.id;
    document.getElementById("firstName").value = student.firstName;
    document.getElementById("lastName").value = student.lastName;
    document.getElementById("email").value = student.email;

    const submitBtn = document.getElementById("submitBtn");
    submitBtn.textContent = "Update";
    submitBtn.style.backgroundColor = "#007bff";

    clearValidationStyles();
  } catch (error) {
    console.error("Error fetching student details: ", error);
  }
}

/**
 * Updates an existing student record.
 *
 * @async
 * @param {number|string} id - The identifier of the student to update.
 * @param {Object} studentData - The updated student details.
 * @param {string} studentData.firstName - The student's first name.
 * @param {string} studentData.lastName - The student's last name.
 * @param {string} studentData.email - The student's email address.
 * @returns {Promise<void>} Resolves after the update request completes.
 */
async function updateStudent(id, studentData) {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(studentData),
      credentials: "include"
    });

    if (response.ok) {
      alert("Student update successfully!");
      resetForm();
      getAllStudents();
      window.location.href = "index.html";
    } else {
      alert("Error updating student.");
    }
  } catch (error) {
    console.error("Error updating student: ", error);
  }
}

/**
 * Deletes a student after the user confirms the action.
 *
 * @async
 * @param {number|string} id - The identifier of the student to delete.
 * @returns {Promise<void>} Resolves after the delete request completes.
 */
async function deleteStudent(id) {
  if (confirm("Are you sure you want to delete this student?")) {
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        credentials: "include"
      });

      if (response.ok) {
        const message = await response.text();
        alert(message);
        getAllStudents();
        window.location.href = "index.html";
      } else {
        alert("Error deleting student.");
      }
    } catch (error) {
      console.error("Error deleting student: ", error);
    }
  }
}

/**
 * Authenticates a user and establishes the login session.
 *
 * @async
 * @param {Object} loginDto - The login credentials.
 * @param {string} loginDto.username - The user's username.
 * @param {string} loginDto.password - The user's password.
 * @returns {Promise<void>} Resolves after the login request completes.
 */
async function loginUser(loginDto) {
  try {
    const response = await fetch(`${API_AUTH}/auth_login_session`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(loginDto),
      credentials: "include"
    });

    if (response.ok) {
      alert("Login successful!");
      getAllStudents();
      window.location.href = "index.html";
    } else {
      alert("Invalid credentials.");
    }
  } catch (error) {
    console.error("Login error: ", error);
  }
}

/**
 * Logs out the current user and clears the student table and form.
 *
 * @async
 * @returns {Promise<void>} Resolves after logout processing completes.
 */
async function logoutUser() {
  try {
    await fetch(`${API_AUTH}/logout`, {
      method: "POST",
      credentials: "include"
    });

    document.getElementById("studentTableBody").innerHTML = "";
    resetForm();
    alert("Logged out successfully!");

    try {
      await fetch(API_URL, {
        method: "GET",
        headers: {
          "Authorization": "Basic " + btoa("invalid_user:wrong_password")
        }
      });
    } catch (err) {
      console.log("Browser credential cache cleared.");
    }
    window.location.reload();

  } catch (e) {
    console.error("Logout transmission crash: ", e);
  }
}