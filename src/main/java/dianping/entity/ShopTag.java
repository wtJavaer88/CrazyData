package dianping.entity;

public class ShopTag
{
    private int shopId;
    private String tagName;// 标签名
    private int upvote;// 投票数
    private boolean isPositive;// 是否积极的标签

    public int getShopId()
    {
        return shopId;
    }

    public ShopTag( int shopId,String tagName,int upvote,boolean isPositive )
    {
        super();
        this.shopId = shopId;
        this.tagName = tagName;
        this.upvote = upvote;
        this.isPositive = isPositive;
    }

    public void setShopId( int shopId )
    {
        this.shopId = shopId;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName( String tagName )
    {
        this.tagName = tagName;
    }

    public int getUpvote()
    {
        return upvote;
    }

    public void setUpvote( int upvote )
    {
        this.upvote = upvote;
    }

    public boolean isPositive()
    {
        return isPositive;
    }

    public void setPositive( boolean isPositive )
    {
        this.isPositive = isPositive;
    }

    @Override
    public String toString()
    {
        return "ShopTag [shopId=" + shopId + ", tagName=" + tagName
                + ", upvote=" + upvote + ", isPositive=" + isPositive + "]";
    }

}

