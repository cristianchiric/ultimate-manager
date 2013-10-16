var dt;
var timelineActions = Array();
function init_timeline() {

	for(var i in timelineActions){
		$('#'+timelineActions[i].id).b1njTimeline({
			'height' : 2200,
			'num_years' : 100
		});
	}
}

$(function() {
	
	d3.json("data/actions.json", function(actions){
		/*
		* Firt step - rearrange the data to manipulate it easier
		* 	-> group by player id
		*/
		dt = d3.nest()
			.key(function(d) { return d.player_id; })
			.sortKeys(d3.ascending)
			.rollup(function(d) {
			  return {
				actions: d,
				player_name: d[0].player_name,
				total_points: d3.sum(d,function(g) {return g.total_points/g.occurences;})
			  };
			})
			.entries(actions);

		var player_node = d3.select('#players')
			.selectAll('div')
			.data(dt)
			.enter()
			.append('div')
			.attr('class','panel panel-default')
			.attr('data-player-id',function(data) {return data.key;});
		player_node.append('div')
			.attr('class','panel-heading')
			.append('h4')
			.attr('class','panel-title')
			.append('a')
			.attr('class','accordion-toggle glyphicon glyphicon-user')
			.attr('data-toggle','collapse')
			.attr('data-parent','#players')
			.attr('title',function(data) {return 'Score: '+data.values.total_points;})
			.attr('href',function(data) {return '#player-'+data.key;})
			.text(function(data){
				return data.values.player_name
			})
			.append('span')
			.attr('data-toggle','tooltip')
			.attr('title','score')
			.attr('class','badge pull-right')
			.text(function(data){
				return data.values.total_points
			});
		var timeline = player_node.append('div')
			.attr('id',function(data) {return 'player-'+data.key;})
			.attr('class','panel-collapse collapse')
			.append('div')
			.attr('class','panel-body')
			.append('ol')
			.attr('id', function(data){
				var tl = {
					id: 'timeline-'+data.key
				};
				timelineActions.push(tl);
				return "timeline-" + data.key;
			})
			.selectAll('li')
			.data(function(d){return d.values.actions})
			.enter()
			.append('li')
			.sort(function (a,b) {return d3.ascending(a.minutes, b.minutes)});
		timeline.append('time')
			.attr('datetime',function(data, index){
				return data.minutes;
			})
			.text(function(data){
				return "'"+data.minutes + ": " + (data.total_points/data.occurences) + " points";
			});
		timeline.append('p')
			.text(function(data){
				return data.action_name;
			});
		
		setTimeout(init_timeline, 500);
	});
	
	
});