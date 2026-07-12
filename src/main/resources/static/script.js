const analyzeBtn = document.getElementById("analyzeBtn");

analyzeBtn.addEventListener("click", uploadFiles);

async function uploadFiles() {

    const applications = document.getElementById("applications").files[0];
    const dependencies = document.getElementById("dependencies").files[0];
    const vulnerabilities = document.getElementById("vulnerabilities").files[0];
    const licenses = document.getElementById("licenses").files[0];
    const transitive = document.getElementById("transitive").files[0];

    if (!applications || !dependencies || !vulnerabilities || !licenses || !transitive) {
        alert("Please upload all required files.");
        return;
    }

    const formData = new FormData();

    formData.append("applications", applications);
    formData.append("dependencies", dependencies);
    formData.append("vulnerabilities", vulnerabilities);
    formData.append("licenses", licenses);
    formData.append("transitive", transitive);

    try {

        showLoading();

        const response = await fetch(
            "http://localhost:8080/api/upload",
            {
                method:"POST",
                body:formData
            }
        );

        await response.text();
        await loadApplications();

    } catch (e) {

        hideLoading();
        console.error(e);
        document.getElementById("applicationBody").innerHTML =
            `<tr>
        
            <td colspan="9" style="color:red;font-weight:bold">
        
                Upload Failed.
 
            </td>
        
        </tr>`;

    }finally{
        hideLoading();
    }

}
function showLoading(){
    document
        .getElementById("loadingOverlay")
        .classList.add("show");
}

function hideLoading(){
    document
        .getElementById("loadingOverlay")
        .classList.remove("show");
}
async function loadApplications() {

    const response = await fetch("http://localhost:8080/api/analyze/all");
    const reports = await response.json();
    const body = document.getElementById("applicationBody");

    body.innerHTML = "";

    reports.forEach((app, index) => {

        body.innerHTML += `

        <tr>

            <td>${index + 1}</td>

            <td>${app.applicationId}</td>

            <td>${app.applicationName}</td>

            <td>${app.riskScore}</td>

            <td>

                <span class="badge ${app.riskLevel.toLowerCase()}">

                    ${app.riskLevel}

                </span>

            </td>

            <td>${app.vulnerableLibraries}</td>

            <td>${app.licenseIssues}</td>

            <td>${app.outdatedLibraries}</td>

            <td>

                <button class="viewBtn"

                    onclick="viewApplication('${app.applicationId}')">

                    View

                </button>

            </td>

        </tr>

        `;

    });

}

async function viewApplication(appId) {

    const response = await fetch(

        "http://localhost:8080/api/test/" + appId

    );
    const report = await response.json();

    document.getElementById("detailsModal").style.display = "block";
    document.getElementById("appName").innerText = report.applicationName;
    document.getElementById("riskScore").innerText = report.riskScore;
    document.getElementById("riskLevel").innerText = report.riskLevel;
    document.getElementById("vulCount").innerText = report.vulnerableLibraries;
    document.getElementById("licenseCount").innerText = report.licenseIssues;
    document.getElementById("outdatedCount").innerText = report.outdatedLibraries;
    document.getElementById("transitiveCount").innerText = report.transitiveIssues;

    const vulTable = document.getElementById("vulnerabilityBody");
    const licenseTable = document.getElementById("licenseBody");
    const outdatedTable = document.getElementById("outdatedBody");
    const recommendationTable = document.getElementById("recommendationBody");

    vulTable.innerHTML = "";
    licenseTable.innerHTML = "";
    outdatedTable.innerHTML = "";
    recommendationTable.innerHTML = "";

    report.issues.forEach(issue => {

        if (issue.includes("CVE")) {

            let cve = issue.split(" ")[0];

            let tech = issue.substring(
                issue.lastIndexOf(" ") + 1
            );

            vulTable.innerHTML += `

                <tr>

                    <td>${cve}</td>

                    <td>${tech}</td>

                </tr>

            `;

        }

        else if (issue.includes("license")) {

            let tech = issue.split(" ")[0];
            let license = issue.split("uses ")[1]
                .split(" license")[0];

            licenseTable.innerHTML += `

                <tr>

                    <td>${tech}</td>

                    <td>${license}</td>

                    <td>Not Compatible</td>

                </tr>

            `;

        }
        else if (issue.includes("updated")) {

            let tech = issue.split(" ")[0];
            outdatedTable.innerHTML += `

                <tr>

                    <td>${tech}</td>

                    <td>Older than 2 Years</td>

                </tr>

            `;

        }

    });

    report.recommendations.forEach(rec => {

        let tech = "";
        if(rec.startsWith("Upgrade")){

            tech = rec.split(" ")[1];

        }
        else if(rec.startsWith("Replace")){

            tech = rec.split(" ")[1];

        }
        recommendationTable.innerHTML += `

            <tr>

                <td>${tech}</td>

                <td>${rec}</td>

            </tr>

        `;

    });

}

function closeModal(){
    document.getElementById("detailsModal").style.display = "none";
}

window.onclick = function(event){
    const modal = document.getElementById("detailsModal");
    if(event.target === modal){
        modal.style.display = "none";
    }
}