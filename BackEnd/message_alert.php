<?php
	//$ActivityId = file_get_contents("php://input");
	
	require_once('./db.php');
	require_once('./response.php');
	$userid = isset($_POST['userid']) ? $_POST['userid'] : -1;
	
	$connect = Db::connect();
    
	//get message
	$sql_message = "select message, activityName, activityReleaseTime, activityDeadline,
	activityTime, activityAddress, activityIntroduction, aSurplusQuota from alert, activity
	where userid = $userid and alert.activityid = activity.ActivityId";
	$result = mysqli_query( $connect, $sql_message );
    
	while($row = mysqli_fetch_assoc($result)) {
		$data[] = $row;
	} 
	
	if($data){
		return response::show(200, 'success', $data);
	}else{
		return Response::show(400, 'fail',$data);
	}
		
	exit;
?>