INSERT INTO um_user
            (id, ol_version, username, password, password_hint, firstName, lastName,
             email, phone_number, website, address, enabled, account_expired, account_locked, credentials_expired,
             about_me, create_time, change_time, affiliation, intended_use
            )
     VALUES (1, 1, 'admin', '026ad9b14a7453b7488daa0c6acbc258b1506f52c441c7c465474c1a564394ff', 'no hint', 'Chris', 'Cross',
             'cris61@gmail.com', '00125252525', 'google.com', '125 NE Main', true, false, false, false,
             'Hooray it works!', TIMESTAMP '2011-03-01 12:00:00', null, 'PSU', 'For Fun'
            );

INSERT INTO um_user
            (id, ol_version, username, password, password_hint, firstName, lastName,
             email, phone_number, website, address, enabled, account_expired, account_locked, credentials_expired,
             about_me, create_time, change_time, affiliation, intended_use
            )
     VALUES (2, 1, 'testuser', 'ae5deb822e0d71992900471a7199d0d95b8e7c9d05c40a8245a281fd2c1d6684', 'so obvious :) ', 'John', 'Green',
             'john@green.com', '00432323232323', null, '123 N Usual Street, 99999 Springfield', true, false, false, false,
             'Just for fun', TIMESTAMP '2011-03-01 12:00:00', null, 'PSU', 'For Fun'
            );
