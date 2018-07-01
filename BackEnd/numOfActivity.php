<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();
	
	//返回组织发起过的活动数量
	
	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$activityId = $arr['actId'];
	//test
	//$activityId = 1;
	
	/*
	orgname– string– 组织的名称
	actnum– string– 组织发起过的活动数
	orgdes– string– 组织介绍
	orgimg– string– 组织图片url
	*/
	$sql = "select groupName,count(activitySponsor) num,groupIntro,groupImage
	from groups,activity
	where groupId = activitySponsor and activityId = '".$activityId."' ";
	$result = mysqli_query ( $conn, $sql );
	
		if(mysqli_num_rows($result) ==1){
			while($row = mysqli_fetch_row($result)) {
				$rows=array(
					'orgname'=>$row[0],
					'actnum'=>$row[1],
					'orgdes'=>$row[2],
					'orgimg'=>$row[3],
				);
			} 
			return Response::show(200,'true',$rows);
		}else if(mysqli_num_rows($result) > 1){
			return Response::show(401, 'false','false,data:illegal data');
		}


	$conn->close();
?> 