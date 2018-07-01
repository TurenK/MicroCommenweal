<?php
header("Content-type:text/html;charset=utf8");
require_once('./db.php');
require_once('./response.php');
require_once('./history.php');
$connect = db::connect();

$json = file_get_contents('php://input');
$arr = json_decode($json, true);
$userId = $arr['userId'];

$pageSize = isset($_POST['pagesize']) ? $_POST['pagesize'] : 5;
if(!is_numeric($pageSize)) {
	return Response::show(401, 'false', 'illegal data');
}
//$userId = 2;//测试
$which = 1;
	try {
		$connect = db::connect();
	} catch(Exception $e) {
		return Response::show(402,'false' , 'database connect error');
	}
	$activities = history::getListinfo($userId, $pageSize, $connect, $which); 

if($activities) {
	return Response::show(200, 'true', $activities);
} else {
	return Response::show(400, 'false', 'no data');
}
?>