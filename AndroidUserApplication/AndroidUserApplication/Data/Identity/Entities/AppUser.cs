using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace AndroidUserApplication.Data.Identity.Entities
{
    public class AppUser : IdentityUser<long>
    {
        [Required, StringLength(255)]
        public string FirstName { get; set; }
        [Required, StringLength(255)]
        public string LastName { get; set; }
        [Required, StringLength(255)]
        public string Image { get; set; }
        [Required, StringLength(255)]
        public string Phone { get; set; }
        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
