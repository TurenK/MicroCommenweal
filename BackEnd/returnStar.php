<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	require_once('./returnStarSQL.php');
	
	try {
		$conn = db::connect();
	} catch(Exception $e) {
		return Response::show(402, 'false','database connect error');
	}

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$userId = $arr['userId'];
	$groupId = $arr['groupId'];
	//test
	//$userId = 31;
	//$groupId = 1;
	
	if( $userId != null ){
		$star = Recommand::getListinfo( $conn, $userId, 1 ); ;
	}else if( $groupId != null ){
//		return response::show(200,'true','666');
		$star = Recommand::getListinfo( $conn, $groupId, 2 ); ;
	}else{
		return Response::show(400, 'false', 'illegal userId or groupId');
	}
	
	if ($star) {
		return response::show(200,'true',$star);
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 