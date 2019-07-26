package co.com.geo.uservalidator.data.db.dao;

import java.util.List;

import co.com.geo.uservalidator.data.model.UserEntity;

public interface IUserEntityDao {
    public UserEntity fetchUserEntityById(int userId);
    public List<UserEntity> fetchAllUsersEntity();
    public boolean addUserEntity(UserEntity user);
    public boolean deleteAllUsersEntity();
}
