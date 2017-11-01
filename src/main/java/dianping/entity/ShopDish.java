package dianping.entity;

public class ShopDish
{
    private int shopId;
    private int dishId;
    private int upvote;
    private String dishName;
    private String url;

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId( int shopId )
    {
        this.shopId = shopId;
    }

    public int getDishId()
    {
        return dishId;
    }

    public void setDishId( int dishId )
    {
        this.dishId = dishId;
    }

    public int getUpvote()
    {
        return upvote;
    }

    public void setUpvote( int upvote )
    {
        this.upvote = upvote;
    }

    public String getDishName()
    {
        return dishName;
    }

    public void setDishName( String dishName )
    {
        this.dishName = dishName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public ShopDish( int shopId,int dishId,int upvote,String dishName,String url )
    {
        super();
        this.shopId = shopId;
        this.dishId = dishId;
        this.upvote = upvote;
        this.dishName = dishName;
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ShopDish [shopId=" + shopId + ", dishId=" + dishId
                + ", upvote=" + upvote + ", dishName=" + dishName + ", url="
                + url + "]";
    }

}

