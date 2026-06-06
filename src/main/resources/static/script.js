console.log("JS loaded successfully");

const API = '/api';
let currentStudentId = parseInt(localStorage.getItem('studentId') || '1');

window.onload = function () {
    const isDashboard = document.getElementById('page-dashboard') !== null;
    if (isDashboard) {
        loadStudentProfile();
        loadAllSkills();
        loadBrowseSkills();
        loadRequests();
        loadSessionsPage();
        loadLeaderboard();
    }
};

function showPage(page, event) {
    document.querySelectorAll('.page-section').forEach(el => {
        el.classList.remove('active');
    });

    document.querySelectorAll('.nav-item').forEach(el => {
        el.classList.remove('active');
    });

    const targetPage = document.getElementById('page-' + page);
    if (targetPage) targetPage.classList.add('active');

    if(event){
        event.currentTarget.classList.add('active');
    }

    if(page==='skills') loadAllSkills();
    if(page==='requests') loadRequests();
    if(page==='sessions') loadSessionsPage();
    if(page==='leaderboard') loadLeaderboard();
}

function loadStudentProfile() {
    fetch(`${API}/students/${currentStudentId}`)
        .then(response => {
            if (!response.ok) throw new Error("Failed to load student");
            return response.json();
        })
        .then(data => {
            const sidebar = document.getElementById("sidebar-name");
            const dash = document.getElementById("dash-name");
            if (sidebar) sidebar.innerText = data.name;
            if (dash) dash.innerText = data.name;
        })
        .catch(err => console.log("Profile load failed:", err));
}

function loadAllSkills() {
    const skillsContainer = document.getElementById("dash-skills");
    if (!skillsContainer) return;

    fetch(`${API}/skills/all`)
        .then(response => response.json())
        .then(data => {
            skillsContainer.innerHTML = "";
            if (data.length === 0) {
                skillsContainer.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">No skills posted yet</div>`;
                return;
            }
            data.forEach(skill => {
                skillsContainer.innerHTML += `
                    <div class="skill-item">
                        <div class="skill-left">
                            <div>
                                <div class="skill-name">${escapeHTML(skill.skillName)}</div>
                                <div class="skill-meta">${escapeHTML(skill.description)}</div>
                            </div>
                        </div>
                        <button class="req-btn" onclick="sendRequestFromScript(${skill.student?.studentId || 1}, '${escapeJS(skill.skillName)}')">Request</button>
                    </div>
                `;
            });
        })
        .catch(err => {
            skillsContainer.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">Error loading skills</div>`;
        });
}

function loadBrowseSkills() {
    const list = document.getElementById("all-skills-list");
    if (!list) return;

    fetch(`${API}/skills/all`)
        .then(response => response.json())
        .then(data => {
            list.innerHTML = "";
            if (data.length === 0) {
                list.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">No skills available</div>`;
                return;
            }
            data.forEach(skill => {
                list.innerHTML += `
                    <div class="skill-item">
                        <div>
                            <div class="skill-name">${escapeHTML(skill.skillName)}</div>
                            <div class="skill-meta">${escapeHTML(skill.student?.name || 'Anonymous')} - ${escapeHTML(skill.description)}</div>
                        </div>
                        <div>
                            <span class="skill-badge ${(skill.skillLevel || 'beginner').toLowerCase()}">${escapeHTML(skill.skillLevel || "Beginner")}</span>
                            <button class="req-btn" onclick="sendRequestFromScript(${skill.student?.studentId || 1}, '${escapeJS(skill.skillName)}')">Request</button>
                        </div>
                    </div>
                `;
            });
        });
}

function loadRequests() {
    const requestContainer = document.getElementById("dash-requests");
    const requestList = document.getElementById("all-requests-list");

    if (!requestContainer && !requestList) return;

    fetch(`${API}/requests/received/${currentStudentId}`)
        .then(response => response.json())
        .then(data => {
            if (requestContainer) {
                requestContainer.innerHTML = "";
                if (data.length === 0) {
                    requestContainer.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">No incoming requests</div>`;
                }
            }
            if (requestList) {
                requestList.innerHTML = "";
                if (data.length === 0) {
                    requestList.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">No requests found</div>`;
                }
            }

            data.forEach(req => {
                let html = `
                    <div class="req-item">
                        <div class="req-info">
                            <div class="req-name">${escapeHTML(req.sender?.name || 'Student')} wants to learn ${escapeHTML(req.skillName)}</div>
                            <div class="req-skill">Status: <span class="status-pill ${(req.status || 'pending').toLowerCase()}">${escapeHTML(req.status)}</span></div>
                        </div>
                        ${req.status === 'PENDING' ? `
                        <div class="action-btns">
                            <button class="accept-btn" onclick="updateReqFromScript(${req.requestId}, 'ACCEPTED')">✓</button>
                            <button class="reject-btn" onclick="updateReqFromScript(${req.requestId}, 'REJECTED')">✕</button>
                        </div>` : ''}
                    </div>
                `;

                if (requestContainer) requestContainer.innerHTML += html;
                if (requestList) requestList.innerHTML += html;
            });
        });
}

function loadSessionsPage() {
    const box = document.getElementById("sessions-list");
    if (!box) return;

    fetch(`${API}/sessions/learner/${currentStudentId}`)
        .then(response => response.json())
        .then(learnerSessions => {
            box.innerHTML = "";
            if (learnerSessions.length === 0) {
                box.innerHTML = `<div style="color:var(--text-muted); font-size:13px; text-align:center; padding:20px;">No sessions booked yet</div>`;
                return;
            }
            learnerSessions.forEach(session => {
                box.innerHTML += `
                    <div class="session-card">
                        <div class="session-top">
                            <span class="session-title">Session with ${escapeHTML(session.teacher?.name || 'Teacher')} (Learning)</span>
                            <span class="status-pill ${(session.status || 'scheduled').toLowerCase()}">${escapeHTML(session.status)}</span>
                        </div>
                        <div class="session-meta">Date: ${escapeHTML(session.date || '')} ${escapeHTML(session.time || '')} | Mode: ${escapeHTML(session.mode)}</div>
                        <div class="session-meta">Link: <a href="${escapeHTML(session.meetingLink)}" target="_blank">${escapeHTML(session.meetingLink)}</a></div>
                    </div>
                `;
            });
        });
}

function registerStudent() {
    const nameEl = document.getElementById("name");
    const emailEl = document.getElementById("email");
    const passwordEl = document.getElementById("password");

    if (!nameEl || !emailEl || !passwordEl) return;

    const name = nameEl.value.trim();
    const email = emailEl.value.trim();
    const password = passwordEl.value.trim();

    if (!name || !email || !password) {
        alert("Please fill in all fields!");
        return;
    }

    const payload = { name, email, password };

    fetch(`${API}/students/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) throw new Error("Registration failed");
        return response.json();
    })
    .then(data => {
        alert("Registration Successful! Please login.");
        window.location.href = "login.html";
    })
    .catch(err => {
        alert("Error during registration: Email might already exist.");
        console.error(err);
    });
}

function loginStudent() {
    const emailEl = document.getElementById("loginEmail");
    const passwordEl = document.getElementById("loginPassword");

    if (!emailEl || !passwordEl) return;

    const email = emailEl.value.trim();
    const password = passwordEl.value.trim();

    if (!email || !password) {
        alert("Please fill in all fields!");
        return;
    }

    fetch(`${API}/students/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, {
        method: "POST"
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error("Invalid credentials");
        }
        if (!response.ok) {
            throw new Error("Login failed");
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem('studentId', data.studentId);
        window.location.href = "dashboard.html";
    })
    .catch(err => {
        alert("Login failed: " + err.message);
    });
}

function postSkill() {
    const nameEl = document.getElementById("skillName");
    const descEl = document.getElementById("skillDesc");

    if (!nameEl) return;

    const skillName = nameEl.value.trim();
    const description = descEl ? descEl.value.trim() : "";

    if (!skillName) {
        alert("Please enter a skill name!");
        return;
    }

    const skill = {
        skillName: skillName,
        description: description,
        student: {
            studentId: currentStudentId
        }
    };

    fetch(`${API}/skills/post`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(skill)
    })
    .then(response => {
        if (!response.ok) throw new Error("Post skill failed");
        return response.json();
    })
    .then(() => {
        alert("Skill posted successfully!");
        if (nameEl) nameEl.value = "";
        if (descEl) descEl.value = "";
        loadAllSkills();
        loadBrowseSkills();
    })
    .catch(err => {
        alert("Error posting skill: " + err.message);
    });
}

function loadLeaderboard() {
    const board = document.getElementById("leaderboard-list");
    if (!board) return;

    fetch(`${API}/students/all`)
        .then(response => response.json())
        .then(data => {
            board.innerHTML = "";
            const sorted = data.sort((a, b) => (b.rating || 0) - (a.rating || 0));
            sorted.forEach((student, index) => {
                board.innerHTML += `
                    <div class="leader-item">
                        <div class="rank">${index + 1}</div>
                        <div class="leader-name">${escapeHTML(student.name)}</div>
                        <div class="leader-pts">⭐ ${(student.rating || 0).toFixed(1)}</div>
                    </div>
                `;
            });
        });
}

// Helpers
function sendRequestFromScript(receiverId, skillName) {
    if (receiverId === currentStudentId) {
        alert('Cannot request yourself!');
        return;
    }
    const req = {
        sender: { studentId: currentStudentId },
        receiver: { studentId: receiverId },
        skillName: skillName || 'Skill Request',
        status: 'PENDING'
    };
    fetch(`${API}/requests/send`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(req)
    })
    .then(res => {
        if (!res.ok) throw new Error("Request failed");
        alert('Request sent!');
    })
    .catch(err => alert('Error: ' + err.message));
}

function updateReqFromScript(requestId, status) {
    fetch(`${API}/requests/${requestId}/status?status=${status}`, { method: 'PUT' })
        .then(res => {
            if (!res.ok) throw new Error("Update status failed");
            alert('Request ' + status);
            loadRequests();
        })
        .catch(err => alert('Error: ' + err.message));
}

function escapeHTML(str) {
    if (!str) return "";
    return str.replace(/[&<>'"]/g, 
        tag => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[tag] || tag)
    );
}

function escapeJS(str) {
    if (!str) return "";
    return str.replace(/'/g, "\\'");
}