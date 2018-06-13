<?php
if(empty($_POST["email"]) or empty($_POST["password"])){
	echo("false");
	die();
}
elseif($_POST["email"] == "keterbusiness@gmail.com" and $_POST["password"] == "abcdef"){
	echo("true");
}
?>