<?php
header("Content-type:text/html;charset=utf8");
require_once('./db.php');
require_once('./response.php');
require_once('./history.php');
$connect = db::connect();

$json = file_get_contents('php://input');
$arr = json_decode($json, true);
$groupId = $arr['groupId'];

$pageSize = isset($_POST['pagesize']) ? $_POST['pagesize'] : 1000;
if(!is_numeric($pageSize)) {
	return Response::show(401, 'false', 'illegal data');
}
//$groupId = 1;//测试
$which = 0;
	try {
		$connect = db::connect();
	} catch(Exception $e) {
		return Response::show(402,'false' , 'database connect error');
	}
	$activities = history::getListinfo($groupId, $pageSize, $connect, $which); 

if($activities) {
	return Response::show(200, 'true', $activities);
} else {
	return Response::show(400, 'false', 'data deliver fail');
}
?>