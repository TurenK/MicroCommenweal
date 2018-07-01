<?php
header('Content-type: application/json;charset=utf-8');
require_once('./response.php');
 
if(empty($_FILES)) 
	return Response::show(407,"false","no files received");
 
$dirPath = './img/';//设置文件保存的目录
 
if(!is_dir($dirPath)){
  //目录不存在则创建目录
  @mkdir($dirPath);
}
 
$count = count($_FILES);//所有文件数

$success = $failure = 0;
 
if($count !== 1) 
	return Response::show(408,"false","more than one received");
 
foreach($_FILES as $key => $value){
  //循环遍历数据
  $tmp = $value['name'];//获取上传文件名
  $tmpName = $value['tmp_name'];//临时文件路径
  //上传的文件会被保存到php临时目录，调用函数将文件复制到指定目录
  $realpath = $dirPath.date('YmdHis').'_'.$tmp;
  if(move_uploaded_file($tmpName,$realpath)){
    $success++;
  }else{
    $failure++;
  }
}

if($success > 0){
	return Response::show(200,"true", $realpath);
}else{
	return Response::show(400,"false","data deliver fail");
}
?>