<?php
	require_once('./db.php');
	require_once('./response.php');
	require_once('./recommandAl.php');
	require_once('./recommandAlGroup.php');
	$connect = db::connect();

	$json = file_get_contents ( 'php://input' );  
	$obj = json_decode ( $json,true ); 
	$userId = $obj['userId'];
	$groupId = $obj['groupId'];
	$activityType = $obj['category'];
	
	//test
//	$userId = "1";
	//$activityType = '有毛病';
//	echo $userId;
	
	//return Response::show(200, 'true', $userId.$activityType);
	
	$pageSize = isset($_POST['pagesize']) ? $_POST['pagesize'] : 100;	
	if(!is_numeric($pageSize)) {
		return Response::show(401, 'illegal data');
	}

	try {
		$connect = Db::connect();
	} catch(Exception $e) {
		return Response::show(402, 'false','database connect error');
	}
	if( $userId != null ){
		$activities = Recommand::getListinfo($pageSize, $connect, $userId, $activityType); 
	}else if( $groupId != null ){
		$activities = RecommandGroup::getListinfo($pageSize, $connect, $groupId, $activityType); 
	}else{
		return Response::show(400, 'false', 'no user or group');
	}
	
	return Response::show(200, 'true', $activities);
	if($activities) {
		return Response::show(200, 'true', $activities);
	} else {
		return Response::show(400, 'false', 'data deliver fail');
	}
		
	$connect->close();
?>