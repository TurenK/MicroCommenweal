<?php
	//$ActivityId = file_get_contents("php://input");
	$ActivityId = 1;
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();
    
	//activity information
	$activity = "select activitySponsor,activityName,activityImage,activityReleaseTime,activityDeadline,
	activityTime, activityAddress, activityIntroduction, aNeedNumOfPerson, aSurplusQuota 
	from activity where activityId = $ActivityId";
	$result = mysqli_query( $connect, $activity );
    
	//remarks
	$remarks = "select userId, remarks from participate where activityId = $ActivityId"; 
	$result1 = mysqli_query($connect, $remarks);
	
	//join user
	$join = "select p.userId, u.userImage from participate p, user u where activityId = '$ActivityId'";
	$result2 = mysqli_query($connect, $join);
    
	//将结果集转为数组，每次取一行
	while($row = mysqli_fetch_assoc($result)) {
		$rows[] = $row;
	} 
	while($row1 = mysqli_fetch_assoc($result1)) {
		$rows1[] = $row1;
	}
	while($row2 = mysqli_fetch_assoc($result2)) {
		$rows2[] = $row2;
	}

	$rows[]["remarks"]=$rows1;
	$rows[][]["join"]=$rows2;
	if($rows){
		return response::show(200, 'success', $rows);
	}else{
		return Response::show(400, 'fail',$rows);
	}
		
	exit;
?>