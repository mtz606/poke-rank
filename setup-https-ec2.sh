#!/bin/bash
# Complete HTTPS Setup for pokemon-rankings.com on EC2
# Run this after DNS is set to "DNS only" in Cloudflare

DOMAIN="pokemon-rankings.com"
EMAIL="your-email@example.com"  # Replace with your email

echo "=========================================="
echo "Setting up HTTPS for $DOMAIN"
echo "=========================================="

# Step 1: Install Certbot
echo "Installing Certbot..."
sudo yum install -y certbot python3-certbot-nginx

# Step 2: Get SSL certificate
echo "Getting SSL certificate..."
sudo certbot --nginx -d $DOMAIN --non-interactive --agree-tos --email $EMAIL

# Step 3: Verify Nginx config
echo "Testing Nginx configuration..."
sudo nginx -t

# Step 4: Restart Nginx
echo "Restarting Nginx..."
sudo systemctl restart nginx

# Step 5: Set up auto-renewal
echo "Setting up certificate auto-renewal..."
(crontab -l 2>/dev/null; echo "0 0,12 * * * certbot renew --quiet --nginx") | crontab -

echo "=========================================="
echo "SSL setup complete!"
echo "Your API is now available at: https://$DOMAIN/api"
echo "=========================================="
echo ""
echo "Next steps:"
echo "1. Test: curl https://$DOMAIN/api/auth/test"
echo "2. In Cloudflare, set SSL mode to 'Full' or 'Full (strict)'"
echo "3. Re-enable Cloudflare proxy (orange cloud) if desired"
echo "4. Update frontend API URL to: https://$DOMAIN/api"

