@echo off

set BASE_URL=http://localhost:8080/engine-rest/deployment/create
set DEPLOYMENT_NAME=my-forms-deployment

curl -X POST %BASE_URL% ^
  -F "deployment-name=%DEPLOYMENT_NAME%" ^
  -F "auth_menu.form=@auth_menu.form" ^
  -F "cart_menu.form=@cart_menu.form" ^
  -F "category_list.form=@category_list.form" ^
  -F "courier_menu.form=@courier_menu.form" ^
  -F "courier_orders.form=@courier_orders.form" ^
  -F "order_history.form=@order_history.form" ^
  -F "products_list.form=@products_list.form" ^
  -F "shop_menu.form=@shop_menu.form" ^
  -F "shopping_cart_list.form=@shopping_cart_list.form" ^
  -F "start_menu.form=@start_menu.form"

pause