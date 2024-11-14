// FILE: src/UserForm.js
import React, { useState, useEffect } from 'react';

const UserForm = () => {
  const [users, setUsers] = useState([]);
  const [formData, setFormData] = useState({ id: '', name: '', description: '' });

  useEffect(() => {
    // Fetch users from the backend
    fetch('http://localhost:8080/user/all')
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setUsers(data))
      .catch(error => {
        console.error('Error fetching users:', error);
      });
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Create or update user
    const method = formData.id ? 'PUT' : 'POST';
    fetch(`http://localhost:8080/user${formData.id ? `/${formData.id}` : '/add'}`, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setUsers([...users.filter(user => user.id !== data.id), data]);
        setFormData({ id: '', name: '', description: '' });
      })
      .catch(error => {
        console.error('Error saving user:', error);
      });
  };

  const handleEdit = (user) => {
    setFormData(user);
  };

  const handleDelete = (id) => {
    // Delete user
    fetch(`http://localhost:8080/user/${id}`, { method: 'DELETE' })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        setUsers(users.filter(user => user.id !== id));
      })
      .catch(error => {
        console.error('Error deleting user:', error);
      });
  };

  return (
    <div>
      <h2>User Form</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="description"
          placeholder="Description"
          value={formData.description}
          onChange={handleChange}
        />
        <button type="submit">{formData.id ? 'Update' : 'Create'}</button>
      </form>
      <h2>User List</h2>
      <ul>
        {users.map(user => (
          <li key={user.id}>
            {user.name} - {user.description}
            <button onClick={() => handleEdit(user)}>Edit</button>
            <button onClick={() => handleDelete(user.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserForm;