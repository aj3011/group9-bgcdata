var AllTeamStrings = [];
function parseTeamString(MorS){
var table = document.getElementById("WorkItemViewtable");
var rows = table.rows;
if(AllTeamStrings.length == 0){
    for(var i = 1; i < rows.length; i++){
        AllTeamStrings[(i-1)] = rows[i].cells[3].innerHTML;
      }
}
setdate(MorS)
var StartDate = document.getElementById("startdate").valueAsDate;
var EndDate = document.getElementById("enddate").valueAsDate;
var weeks = weeksBetween(StartDate, EndDate);
for(var j = 1; j < rows.length; j++){
var team = AllTeamStrings[(j-1)]
//console.log(team);
createTable(j, team, rows, weeks, StartDate, EndDate);
}
//rows[1].cells[3].innerHTML += "</table>";
}

function weeksBetween(StartDate, EndDate) {
    return Math.ceil((EndDate - StartDate) / (7 * 24 * 60 * 60 * 1000));//rounds up the amount of weeks between the two dates (the number is the amount of milliseconds in a week)
}

function createTable(row, team, rows, weeks, startdate, enddate){
    var i = 0;
    team = team.split("|");
    var Team = "<table><tr><th>Team Name</th><th>Type</th>";
    
    for(var j = 0; j <= weeks; j++){
        var d = startdate.getTime()+((j)*(7 * 24 * 60 * 60 * 1000) + (24 * 60 * 60 * 1000));
        var daterow = new Date(d);
        Team += "<th>"+daterow.toString().substring(0, 15)+"</th>";
    }
    Team += "</tr>"
    var start = weeksBetween(startdate, Date.parse(rows[row].cells[1].innerHTML));
    while(i < weeks){
        var tstart = start;
        if(i < (team.length-1)){
        Team += "<tr>"
        team[i] = team[i].split(",");
        var c = 0;
        team[i].forEach(t => {
            if(c == 0){
                Team += "<td>"+t.slice(3)+"</td> ";
                c++;
            }else{
            if(c == 1){
                Team += "<td>"+t+"</td>";
                c++;
                }
                if(c == 2){
                    for(var m = 0; m < (start); m++){
                        Team += "<td></td>";
                       c++; 
                    }
                     }else{
                        if(tstart < 0){
                        tstart++;
                        }else{
                        if(weeks >= (c-2)){
                        Team += "<td>"+t+"</td>";
                        }
                        }
                        c++;
                }
                }
            });
            for(var m = 0; m <= (weeks-(c-2)); m++){
                Team += "<td></td>"; 
            }
            Team += "</tr>"
        }
    
    i++;
    }
    rows[row].cells[3].innerHTML = Team+"</table>";
}

function setdate(MorS){
    if(MorS == 1){
        var date = document.getElementById("startdate").value;
        var day = new Date(date).getDay();
        date = new Date(date).getTime()-(day* 24 * 60 * 60 * 1000);
        document.getElementById("startdate").valueAsDate = new Date(date);
    }else{
        var date = document.getElementById("enddate").valueAsDate;
        var day = new Date(date).getDay();
        date = new Date(date).getTime() +((6-day)* 24 * 60 * 60 * 1000);
        document.getElementById("enddate").valueAsDate = new Date(date);
    }
}