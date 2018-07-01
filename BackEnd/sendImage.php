<?php
header("Content-type:text/html;charset=utf8");
require_once('./response.php');
require_once('./db.php');

$imageUrl = isset($_POST['filepath']) ? $_POST['filepath'] : "not exists";

if($imageUrl=="not exists"){
	//return Response::show(400,"no url received", $imageUrl);
	return Response::show(411,"false","no url received");
}else{
	try{
	echo readfile($imageUrl);
	}catch(Exception $e){
		return Response::show(412,"false","read file failed");
	}
}
?>