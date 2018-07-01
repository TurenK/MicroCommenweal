<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$groupId = $arr['orgId'];
	//test
	//$groupId=1;
	
	$sql1 = "select groupId, groupName, groupImage,groupIntro from groups where groupId = '".$groupId."'";
	$sql2 = "select SUM(timestampdiff(HOUR,activityStartTime,activityEndTime)) as totalTime from activity where activitySponsor= '".$groupId."' and activity.activityStatus=0 group by activitySponsor"; 
	$sql4 = "select count(activitySponsor) as comNum,avg(ratnum) as grade from activity,evaluateUserToGroup where activitySponsor = '".$groupId."' and activity.activityId=evaluateUserToGroup.activityId";
	$sql3 = "select activityName,activity.activityId,activityImage,
	avg(ratnum) from activity left join evaluateUserToGroup 
	on evaluateUserToGroup.activityId=activity.activityId
	where activity.activitySponsor = ".$groupId." 
	group by activityId";
	$sql5 = "select distinct count(activitySponsor) as actnum from activity where activitySponsor= '".$groupId."' group by activitySponsor"; 
	$result1=mysqli_query($connect, $sql1);
	while ( $row = mysqli_fetch_row($result1)) {
		$rows0=array('orgid'=>$row[0],
        'orgname'=>$row[1],
		'orgimage'=>$row[2],
		'description'=>$row[3]);
	}
	$result2=mysqli_query($connect, $sql2);
	while ( $row = mysqli_fetch_row($result2)) {
		$rows0['totalTime']=(int)$row[0];
		//array_push($rows0,array('actNum'=>$row[0]));
		//array_push($rows0,array('totalTime'=>$row[1]));
	}
	$result5=mysqli_query($connect, $sql5);
	while ( $row = mysqli_fetch_row($result5)) {
		$rows0['actnum']=(int)$row[0];
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
		$eachrow=array('actname'=>$row[0],
        'actid'=>$row[1],
		'actimage'=>$row[2],
		'actcom'=>(int)$row[3]);
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
	