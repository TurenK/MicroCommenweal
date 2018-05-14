<?php
require_once('./db.php');
require_once('./response.php');
require_once('./history.php');
$connect = Db::connect();
$userid = isset($_POST['userid']) ? $_POST['userid'] : -1;
$pageSize = isset($_POST['pagesize']) ? $_POST['pagesize'] : 5;
if(!is_numeric($pageSize)) {
	return Response::show(401, '数据不合法');
}
$userid = 1;//测试
	try {
		$connect = Db::connect();
	} catch(Exception $e) {
		return Response::show(403, '数据库链接失败');
	}
	$activities = history::getListinfo($userid, $pageSize, $connect); 

if($activities) {
	return Response::show(200, '首页数据获取成功', $activities);
} else {
	return Response::show(400, '首页数据获取失败', $activities);
}
?>
	
