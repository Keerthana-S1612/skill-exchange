console.log("JS loaded successfully");

let currentStudentId = 1;

window.onload = function () {
    loadStudentProfile();
    loadAllSkills();
    loadBrowseSkills();
    loadRequests();
    loadSessionsPage();
    loadLeaderboard();
};
function showPage(page, event) {
    document.querySelectorAll('.page-section').forEach(el => {
        el.classList.remove('active');
    });

    document.querySelectorAll('.nav-item').forEach(el => {
        el.classList.remove('active');
    });

    document.getElementById('page-' + page).classList.add('active');

    if(event){
        event.currentTarget.classList.add('active');
    }

    if(page==='skills') loadAllSkills();
    if(page==='requests') loadAllRequests();
    if(page==='sessions') loadSessions();
    if(page==='leaderboard') loadLeaderboard();
}

function loadStudentProfile() {
    fetch(`http://localhost:8080/api/students/${currentStudentId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("sidebar-name").innerText = data.name;
            document.getElementById("dash-name").innerText = data.name;
        });
}

function loadAllSkills() {
    fetch("http://localhost:8080/api/skills/all")
        .then(response => response.json())
        .then(data => {
            let skillsContainer = document.getElementById("dash-skills");
            skillsContainer.innerHTML = "";

            data.forEach(skill => {
                skillsContainer.innerHTML += `
                    <div class="skill-item">
                        <div class="skill-left">
                            <div>
                                <div class="skill-name">${skill.skillName}</div>
                                <div class="skill-meta">${skill.description}</div>
                            </div>
                        </div>
                        <button class="req-btn">Request</button>
                    </div>
                `;
            });
        });
}

function loadBrowseSkills() {
    fetch("http://localhost:8080/api/skills/all")
        .then(response => response.json())
        .then(data => {
            let list = document.getElementById("all-skills-list");
            if (!list) return;

            list.innerHTML = "";

            data.forEach(skill => {
                list.innerHTML += `
                    <div class="skill-item">
                        <div>${skill.skillName}</div>
                        <div>${skill.skillLevel || "Beginner"}</div>
                    </div>
                `;
            });
        });
}

function loadRequests() {
    fetch(`http://localhost:8080/api/requests/received/${currentStudentId}`)
        .then(response => response.json())
        .then(data => {
            let requestContainer = document.getElementById("dash-requests");
            let requestList = document.getElementById("requests-list");

            if (requestContainer) requestContainer.innerHTML = "";
            if (requestList) requestList.innerHTML = "";

            data.forEach(req => {
                let html = `
                    <div class="req-item">
                        <div class="req-info">
                            <div class="req-name">${req.skillName}</div>
                            <div class="req-skill">${req.status}</div>
                        </div>
                    </div>
                `;

                if (requestContainer) requestContainer.innerHTML += html;
                if (requestList) requestList.innerHTML += html;
            });
        });
}

function loadSessionsPage() {
    fetch(`http://localhost:8080/api/sessions/teacher/${currentStudentId}`)
        .then(response => response.json())
        .then(data => {
            let box = document.getElementById("sessions-list");
            if (!box) return;

            box.innerHTML = "";

            data.forEach(session => {
                box.innerHTML += `
                    <div class="session-card">
                        <div>${session.mode}</div>
                        <div>${session.status}</div>
                    </div>
                `;
            });
        });
}

function postSkill() {
    let skill = {
        skillName: document.getElementById("skillName").value,
        description: document.getElementById("skillDesc").value,
        student: {
            studentId: currentStudentId
        }
    };

    fetch("http://localhost:8080/api/skills/post", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(skill)
    })
        .then(response => response.json())
        .then(() => {
            alert("Skill posted successfully");
            loadAllSkills();
            loadBrowseSkills();
        });
}

function loadLeaderboard() {
    fetch("http://localhost:8080/api/students/all")
        .then(response => response.json())
        .then(data => {
            let board = document.getElementById("leaderboard-list");
            if (!board) return;

            board.innerHTML = "";

            data.forEach((student, index) => {
                board.innerHTML += `
                    <div class="leader-item">
                        <div>${index + 1}</div>
                        <div>${student.name}</div>
                    </div>
                `;
            });
        });
}