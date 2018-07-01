<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$userId = $arr['userId'];
	//test
	//$userId=1;
	
	$sql1 = "select userId, userName, userImage, userLabel from user where userId = '".$userId."'";
	$sql2 = "select count(userId),SUM(timestampdiff(HOUR,activityStartTime,activityEndTime)) as totalTime from participate,activity where userId= '".$userId."' and activity.activityId=participate.activityId"; 
	$sql4 = "select count(uId) as comNum,avg(starNum) as grade from organization where uId = '".$userId."'";
	$sql3 = "select groupName,groupId,groupImage,remarks as cominfo,starNum as ratnum from groups,organization where organization.uId = '".$userId."' and organization.gId=groups.groupId";
	$result1=mysqli_query($connect, $sql1);
	while ( $row = mysqli_fetch_row($result1)) {
		$rows0=array('userid'=>$row[0],
        'username'=>$row[1],
		'userimage'=>$row[2],
		'userLabel'=>$row[3]);
	}
	$result2=mysqli_query($connect, $sql2);
	while ( $row = mysqli_fetch_row($result2)) {
		$rows0['actnum']=(int)$row[0];
		$rows0['totalTime']=(int)$row[1];
		//array_push($rows0,array('actNum'=>$row[0]));
		//array_push($rows0,array('totalTime'=>$row[1]));
	}
	$result4=mysqli_query($connect, $sql4);
	while ( $row = mysqli_fetch_row($result4)) {
		$rows0['comnum']=(int)$row[0];
		$rows0['grade']=(int)$row[1];
		//array_push($rows0,array('comNum'=>$row[0]));
		//array_push($rows0,array('grade'=>$row[1]));
	}
	$result3=mysqli_query($connect, $sql3);
	$i=0;
	while ( $row = mysqli_fetch_row($result3)) {
		$eachrow=array('orgname'=>$row[0],
        'orgid'=>$row[1],
		'orgimage'=>$row[2],
		'orgdes'=>$row[3],
		'orgcom'=>(int)$row[4]);
		$rows0[$i] = $eachrow;
		$i++;
	}
	if($rows0){
		response::show(200,'true',$rows0);
	}else{
		return response::show(400,'false','data deliver fail');
	}	
	exit;
?>
	