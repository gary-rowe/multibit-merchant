select
  supplier0_.id as id7_6_,
  user1_.id as id8_0_,
  user1_.api_key as api2_8_0_,
  user1_.created_at as created3_8_0_,
  user1_.customer_id as customer13_8_0_,
  user1_.deleted as deleted8_0_,
  user1_.locked as locked8_0_,
  user1_.password as password8_0_,
  user1_.password_reset_at as password7_8_0_,
  user1_.reasonForDelete as reasonFo8_8_0_,
  user1_.secret_key as secret9_8_0_,
  user1_.session_key as session10_8_0_,
  user1_.staff as staff8_0_,
  user1_.supplier_id as supplier14_8_0_,
  user1_.username as username8_0_,
  contactmet2_.users_id as users1_8_8_,
  contactmet3_.id as contactM2_8_,
  contactmet2_.contactMethodMap_KEY as contactM3_8_,
  contactmet3_.id as id2_1_,
  contactmet3_.primary_detail as primary2_2_1_,
  customer4_.id as id3_2_,
  customer4_.cart_fk as cart2_3_2_,
  cart5_.id as id0_3_,
  cartitems6_.cart_id as cart4_0_9_,
  cartitems6_.cart_id as cart4_9_,
  cartitems6_.item_id as item3_9_,
  cartitems6_.cart_id as cart4_1_4_,
  cartitems6_.item_id as item3_1_4_,
  cartitems6_. index as index1_4_ , cartitems6_ . quantity as quantity1_4_ , userroles7_ . user_id as user2_8_10_ , userroles7_ . role_id as role1_10_ , userroles7_ . user_id as user2_10_ , userroles7_ . role_id as role1_10_5_ , userroles7_ . user_id as user2_10_5_
from suppliers supplier0_
     left outer join users user1_ on supplier0_.id = user1_.supplier_id
     left outer join users_contact_method_details contactmet2_ on user1_.id = contactmet2_.users_id
     left outer join contact_method_details contactmet3_ on contactmet2_.contactMethodMap_id = contactmet3_.id
     left outer join customers customer4_ on user1_.customer_id = customer4_.id
     left outer join carts cart5_ on customer4_.cart_fk = cart5_.id
     left outer join cart_items cartitems6_ on cart5_.id = cartitems6_.cart_id
     left outer join user_roles userroles7_ on user1_.id = userroles7_.user_id
where supplier0_.id = ?
order by cartitems6_. index asc
--
--binding parameter [1] as [BIGINT] - 1
--[3] as column [id8_0_]
--[7] as column [id2_1_]
--[null] as column [id3_2_]
--[null] as column [id0_3_]
--[null] as column [cart4_1_4_]
--[6] as column [role1_10_5_]
--[3] as column [user2_10_5_]
