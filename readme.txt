测试请求
curl --location --request GET 'http://localhost:9112/secret/get' \
--header 'Content-Type: application/json' \
--data '{
        "data":"72A115582C21B9295FBE9E60A2CC6D15"
}'