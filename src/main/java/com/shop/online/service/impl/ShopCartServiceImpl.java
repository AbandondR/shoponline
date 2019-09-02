package com.shop.online.service.impl;

import com.shop.online.dao.CartItemDao;
import com.shop.online.dao.ProductSkuDao;
import com.shop.online.dao.ShopCartDao;
import com.shop.online.model.CartItem;
import com.shop.online.model.ProductSku;
import com.shop.online.model.ShopCart;
import com.shop.online.service.ShopCartService;
import com.shop.online.vo.CartItemVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Resource
    private ShopCartDao shopCartDao;

    @Resource
    private CartItemDao cartItemDao;

    /**
     * 添加指定数量的商品到用户购物车
     * @param productSku
     * @param userId
     * @param productNum
     * @return
     * @throws Exception
     */
    @Override
    public int addProductAndNum(ProductSku productSku, String skuGroupStr, String userId, String productNum) throws Exception {

        //获取当前用户的购物车
        String cartQueryHql = "from ShopCart sc where sc.customerId=:userId";
        ShopCart cart = shopCartDao.queryOne(cartQueryHql, userId);

        //判定当前用户购物车是否装满
        String itemCountHql = "select count(*) from CartItem ci where ci.cartId=:cartId";
        Map<String, String> itemCountParams = new HashMap<>();
        itemCountParams.put("cartId", cart.getId());
        int count = cartItemDao.cartItemCount(itemCountHql, itemCountParams);

        //判断是否已经添加过该商品
        String queryItemHql = "from CartItem ci where ci.skuId=:skuId or ci.productId=:skuId";
        Map<String, String> queryItemParams = new HashMap<>();
        queryItemParams.put("skuId", productSku.getId());
        List<CartItem> items = cartItemDao.queryItem(queryItemHql, queryItemParams);
        if(!items.isEmpty()) {
            CartItem item1 = items.get(0);
            String skuItemCount = item1.getSkuItemCount();
            item1.setSkuItemCount(String.valueOf(Integer.parseInt(productNum)+Integer.parseInt(skuItemCount)));
            cartItemDao.update(item1);
            return 1;
        }

        //超出容量
        if(count > Integer.parseInt(cart.getCapacity()) ||
                (count+Integer.parseInt(productNum)) > Integer.parseInt(cart.getCapacity())) {
            throw new Exception("overtop");
        }

        //新添加的商品
        CartItem item = skuToCartItem(productSku);
        item.setSkuGroupStr(skuGroupStr);
        item.setSkuItemCount(productNum);
        item.setCartId(cart.getId());
        String uuid = String.valueOf(UUID.randomUUID());
        item.setId(uuid.replaceAll("\\-", ""));
        cartItemDao.save(item);
        return 1;
    }

    @Override
    public int deleteItem(String userId, String skuId) throws Exception {
        //获取当前用户的购物车
        String hql = "from ShopCart sc where sc.customerId=:userId";
        ShopCart shopCart = shopCartDao.queryOne(hql, userId);

        String deleteHql = "delete from CartItem ci where ci.cartId=:cartId and ci.skuId=:skuId";
        Map<String, String> params = new HashMap<>();
        params.put("cartId", shopCart.getId());
        params.put("skuId", skuId);
        return cartItemDao.deleteItemBySkuId(deleteHql, params);
    }

    @Resource
    private ProductSkuDao productSkuDao;

    @Override
    public String updateItemCount(String skuId, String itemCount, String userId) throws Exception {
        //获取当前用户的购物车
        String hql = "from ShopCart sc where sc.customerId=:userId";
        ShopCart shopCart = shopCartDao.queryOne(hql, userId);

        //判断库存是否足够
        String skuHql = "from ProductSku ps where ps.id=:skuId";
        ProductSku productSku = productSkuDao.QueryOneById(skuHql, skuId);
        //库存不足
        if(Integer.parseInt(productSku.getStock()) < Integer.parseInt(itemCount)) {
            return "lack_stock";
        }
        //更新itemCount
        String updateHql = "update CartItem ci set ci.skuItemCount=:itemCount where ci.cartId=:cartId and ci.skuId=:skuId";
        Map<String, String> params = new HashMap<>();
        params.put("itemCount", itemCount);
        params.put("cartId", shopCart.getId());
        params.put("skuId", skuId);
        cartItemDao.updateItemCount(updateHql, params);
        return "success";
    }

    @Override
    public List<CartItemVo> queryItemsBySkuId(String userId, String[] skuIds) throws Exception {
        if(skuIds!=null && skuIds.length > 0) {
            //获取当前用户的购物车
            String hql = "from ShopCart sc where sc.customerId=:userId";

            ShopCart shopCart = shopCartDao.queryOne(hql, userId);
            String itemsHql = "select new com.shop.online.vo.CartItemVo(ci.id,ci.price,ci.skuGroupStr,ci.skuItemCount,ci.skuId,ci.productId,ci.cartId, ps.imageLocation,ps.stock, p.productName, p.description)" +
                    "from CartItem ci, ProductSku ps, Product p where ci.cartId=:cartId and ci.skuId in (:skuIds) and ci.productId=p.id and ci.skuId=ps.id";
            List<CartItemVo> cartItemVos = cartItemDao.queryItemsBySkuIds(itemsHql, shopCart.getId(), skuIds);
            for(CartItemVo cartItemVo : cartItemVos) {
                int price = Integer.parseInt(cartItemVo.getPrice());
                int count = Integer.parseInt(cartItemVo.getSkuItemCount());
                cartItemVo.setTotalPrice(String.valueOf(price * count));
            }
            return cartItemVos;
        }
        return null;
    }

    /**
     * 为注册用户添加购物车
     * @param userId
     */
    @Override
    public void generateCart(String userId) {
        ShopCart cart = new ShopCart();
        cart.setCapacity("12");
        cart.setCustomerId(userId);
        shopCartDao.save(cart);
    }

    /**
     * ProductSku convert into CartItem
     * @param productSku
     * @return
     */
    public CartItem skuToCartItem(ProductSku productSku){
        CartItem cartItem = new CartItem();
        cartItem.setSkuId(productSku.getId());
        cartItem.setPrice(productSku.getPrice());
        cartItem.setSkuPropGroup(productSku.getSkuGorup());
        cartItem.setProductId(productSku.getProductId());
        return cartItem;
    }
}
