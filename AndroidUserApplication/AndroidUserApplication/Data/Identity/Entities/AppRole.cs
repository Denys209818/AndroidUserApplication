using Microsoft.AspNetCore.Identity;

namespace AndroidUserApplication.Data.Identity.Entities
{
    public class AppRole : IdentityRole<long>
    {
        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
