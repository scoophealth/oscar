# This script is a reminder to expire the oscardoc user when setting
# up a production environment. This user/password/pin is mentioned
# throughout the documentation.

update security set date_ExpireDate=now() where user_name='oscardoc';

