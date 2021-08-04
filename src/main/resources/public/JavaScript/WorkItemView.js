var AllTeamStrings = [];
function parseTeamString(){
var table = document.getElementById("WorkItemViewtable");
var rows = table.rows;
if(AllTeamStrings.length == 0){
    for(var i = 1; i < rows.length; i++){
        AllTeamStrings[(i-1)] = rows[i].cells[3].innerHTML;
      }
}
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
    
    for(var j = 1; j <= weeks; j++){
        var d = startdate.getTime()+((j)*(7 * 24 * 60 * 60 * 1000) + (24 * 60 * 60 * 1000));
        var daterow = new Date(d);
        Team += "<th>"+daterow.toString().substring(0, 15)+"</th>";
    }
    Team += "</tr>"
    var start = weeksBetween(startdate, Date.parse(rows[row].cells[1].innerHTML));
    var end = weeksBetween(Date.parse(rows[row].cells[2].innerHTML), enddate);
    while(i < weeks){
        var tstart = start;
        var tend = weeks+end;
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
                for(var m = 0; m < (start-1); m++){
                    Team += "<td></td>";
                    c++;
                }
                }else{
                    if(tstart < 0){
                    tstart++;
                    }else{
                    if(weeks >= (c-1)){
                    Team += "<td>"+t+"</td>";
                    }else{
                    Team += "<td></td>";
                    tend++;
                    }
                    }
                    c++;
            }
            }
        });
       for(var m = 0; m < end; m++){
            Team += "<td></td>";
        }
        Team += "</tr>"
    }

    i++;
    }

    //console.log(rows);
    rows[row].cells[3].innerHTML = Team+"</table>";
}